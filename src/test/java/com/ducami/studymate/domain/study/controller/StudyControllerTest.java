package com.ducami.studymate.domain.study.controller;

import com.ducami.studymate.domain.study.entity.StudyEntity;
import com.ducami.studymate.domain.study.repository.StudyRepository;
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

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class StudyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("인증된 사용자는 스터디를 생성하면 owner로 저장된다")
    void createStudyWithOwner() throws Exception {
        UserEntity owner = createUser("study-owner@example.com");
        String accessToken = login(owner.getEmail());

        mockMvc.perform(post("/api/v1/studies")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "스프링 스터디",
                                  "content": "매주 월요일 진행"
                                }
                                """))
                .andExpect(status().isCreated());

        StudyEntity savedStudy = studyRepository.findAll().getFirst();
        assertEquals(owner.getId(), savedStudy.getOwner().getId());
        assertEquals("스프링 스터디", savedStudy.getTitle());
    }

    @Test
    @DisplayName("인증 없이 스터디를 생성할 수 없다")
    void createStudyWithoutToken() throws Exception {
        mockMvc.perform(post("/api/v1/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "스프링 스터디",
                                  "content": "매주 월요일 진행"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status", is(401)));
    }

    @Test
    @DisplayName("스터디 작성자가 아니면 수정할 수 없다")
    void updateStudyByNonOwner() throws Exception {
        UserEntity owner = createUser("owner@example.com");
        UserEntity otherUser = createUser("other@example.com");
        StudyEntity study = studyRepository.save(StudyEntity.builder()
                .title("자바 스터디")
                .content("기초 문법")
                .owner(owner)
                .build());

        String accessToken = login(otherUser.getEmail());

        mockMvc.perform(put("/api/v1/studies/{id}", study.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "수정된 제목"
                                }
                                """))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.message", is("스터디 작성자만 수정/삭제할 수 있습니다.")));
    }

    @Test
    @DisplayName("스터디 제목을 빈 값으로 수정할 수 없다")
    void updateStudyWithBlankTitle() throws Exception {
        UserEntity owner = createUser("owner2@example.com");
        StudyEntity study = studyRepository.save(StudyEntity.builder()
                .title("기존 제목")
                .content("기존 내용")
                .owner(owner)
                .build());
        String accessToken = login(owner.getEmail());

        mockMvc.perform(put("/api/v1/studies/{id}", study.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "   "
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.data.code", is("GLOBAL_400")));
    }

    private UserEntity createUser(String email) {
        return userRepository.save(UserEntity.builder()
                .name("tester")
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
