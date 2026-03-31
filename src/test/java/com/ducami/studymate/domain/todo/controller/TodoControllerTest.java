package com.ducami.studymate.domain.todo.controller;

import com.ducami.studymate.domain.study.entity.StudyEntity;
import com.ducami.studymate.domain.study.repository.StudyRepository;
import com.ducami.studymate.domain.todo.entity.TodoEntity;
import com.ducami.studymate.domain.todo.entity.TodoStatus;
import com.ducami.studymate.domain.todo.repository.TodoRepository;
import com.ducami.studymate.domain.user.entity.UserEntity;
import com.ducami.studymate.domain.user.enums.UserRole;
import com.ducami.studymate.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("스터디에 Todo를 등록하고 목록을 조회한다")
    void createAndFindAll() throws Exception {
        UserEntity owner = createOwner("backend-owner@example.com");
        UserEntity author = createUser("backend-author@example.com");
        StudyEntity study = studyRepository.save(StudyEntity.builder()
                .title("백엔드 스터디")
                .content("매주 목요일 진행")
                .owner(owner)
                .build());
        String accessToken = login(author.getEmail());

        mockMvc.perform(post("/api/v1/studies/{studyId}/todos", study.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "content": "1주차 과제 제출"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is(201)))
                .andExpect(jsonPath("$.message", is("Todo를 등록했습니다.")));

        mockMvc.perform(get("/api/v1/studies/{studyId}/todos", study.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].content", is("1주차 과제 제출")))
                .andExpect(jsonPath("$.data[0].status", is("PENDING")));
    }

    @Test
    @DisplayName("Todo 내용을 수정하고 완료 상태로 변경할 수 있다")
    void updateAndChangeStatus() throws Exception {
        UserEntity owner = createOwner("algo-owner@example.com");
        UserEntity author = createUser("algo-author@example.com");
        StudyEntity study = studyRepository.save(StudyEntity.builder()
                .title("알고리즘 스터디")
                .content("주 2회 문제 풀이")
                .owner(owner)
                .build());
        TodoEntity todo = todoRepository.save(TodoEntity.builder()
                .study(study)
                .author(author)
                .content("백준 10문제 풀기")
                .status(TodoStatus.PENDING)
                .build());
        String accessToken = login(author.getEmail());

        mockMvc.perform(put("/api/v1/studies/{studyId}/todos/{todoId}", study.getId(), todo.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "content": "백준 15문제 풀기"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Todo를 수정했습니다.")));

        mockMvc.perform(patch("/api/v1/studies/{studyId}/todos/{todoId}/status", study.getId(), todo.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "COMPLETED"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Todo 상태를 변경했습니다.")));

        mockMvc.perform(get("/api/v1/studies/{studyId}/todos/{todoId}", study.getId(), todo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", is("백준 15문제 풀기")))
                .andExpect(jsonPath("$.data.status", is("COMPLETED")));
    }

    @Test
    @DisplayName("Todo를 삭제할 수 있다")
    void deleteTodo() throws Exception {
        UserEntity owner = createOwner("cs-owner@example.com");
        UserEntity author = createUser("cs-author@example.com");
        StudyEntity study = studyRepository.save(StudyEntity.builder()
                .title("CS 스터디")
                .content("운영체제 발표 준비")
                .owner(owner)
                .build());
        TodoEntity todo = todoRepository.save(TodoEntity.builder()
                .study(study)
                .author(author)
                .content("세마포어 정리")
                .status(TodoStatus.PENDING)
                .build());
        String accessToken = login(author.getEmail());

        mockMvc.perform(delete("/api/v1/studies/{studyId}/todos/{todoId}", study.getId(), todo.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Todo를 삭제했습니다.")));

        mockMvc.perform(get("/api/v1/studies/{studyId}/todos", study.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @DisplayName("Todo 작성자가 아니면 수정할 수 없다")
    void updateTodoByNonAuthor() throws Exception {
        UserEntity owner = createOwner("todo-owner@example.com");
        UserEntity author = createUser("todo-author@example.com");
        UserEntity otherUser = createUser("todo-other@example.com");
        StudyEntity study = studyRepository.save(StudyEntity.builder()
                .title("네트워크 스터디")
                .content("TCP/IP 발표")
                .owner(owner)
                .build());
        TodoEntity todo = todoRepository.save(TodoEntity.builder()
                .study(study)
                .author(author)
                .content("3-way handshake 정리")
                .status(TodoStatus.PENDING)
                .build());

        String accessToken = login(otherUser.getEmail());

        mockMvc.perform(put("/api/v1/studies/{studyId}/todos/{todoId}", study.getId(), todo.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "content": "수정 시도"
                                }
                                """))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.message", is("Todo 작성자만 수정/삭제/상태변경할 수 있습니다.")));
    }

    @Test
    @DisplayName("인증 없이 Todo를 생성할 수 없다")
    void createTodoWithoutToken() throws Exception {
        UserEntity owner = createOwner("todo-public-owner@example.com");
        StudyEntity study = studyRepository.save(StudyEntity.builder()
                .title("운영체제 스터디")
                .content("프로세스 발표")
                .owner(owner)
                .build());

        mockMvc.perform(post("/api/v1/studies/{studyId}/todos", study.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "content": "문서 정리"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status", is(401)));
    }

    private UserEntity createOwner(String email) {
        return createUser(email);
    }

    private UserEntity createUser(String email) {
        return userRepository.save(UserEntity.builder()
                .name("user")
                .email(email)
                .password(passwordEncoder.encode("password123"))
                .role(UserRole.USER)
                .build());
    }

    private String login(String email) throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "password123"
                                }
                                """.formatted(email)))
                .andExpect(status().isOk())
                .andReturn();

        return loginResult.getResponse()
                .getContentAsString()
                .split("\"accessToken\":\"")[1]
                .split("\"")[0];
    }
}
