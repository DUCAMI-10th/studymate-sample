package com.ducami.studymate.domain.todo.controller;

import com.ducami.studymate.domain.study.entity.StudyEntity;
import com.ducami.studymate.domain.study.repository.StudyRepository;
import com.ducami.studymate.domain.todo.entity.TodoEntity;
import com.ducami.studymate.domain.todo.entity.TodoStatus;
import com.ducami.studymate.domain.todo.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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

    @Test
    @DisplayName("스터디에 Todo를 등록하고 목록을 조회한다")
    void createAndFindAll() throws Exception {
        StudyEntity study = studyRepository.save(StudyEntity.builder()
                .title("백엔드 스터디")
                .content("매주 목요일 진행")
                .build());

        mockMvc.perform(post("/api/v1/studies/{studyId}/todos", study.getId())
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
        StudyEntity study = studyRepository.save(StudyEntity.builder()
                .title("알고리즘 스터디")
                .content("주 2회 문제 풀이")
                .build());
        TodoEntity todo = todoRepository.save(TodoEntity.builder()
                .study(study)
                .content("백준 10문제 풀기")
                .status(TodoStatus.PENDING)
                .build());

        mockMvc.perform(put("/api/v1/studies/{studyId}/todos/{todoId}", study.getId(), todo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "content": "백준 15문제 풀기"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Todo를 수정했습니다.")));

        mockMvc.perform(patch("/api/v1/studies/{studyId}/todos/{todoId}/status", study.getId(), todo.getId())
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
        StudyEntity study = studyRepository.save(StudyEntity.builder()
                .title("CS 스터디")
                .content("운영체제 발표 준비")
                .build());
        TodoEntity todo = todoRepository.save(TodoEntity.builder()
                .study(study)
                .content("세마포어 정리")
                .status(TodoStatus.PENDING)
                .build());

        mockMvc.perform(delete("/api/v1/studies/{studyId}/todos/{todoId}", study.getId(), todo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Todo를 삭제했습니다.")));

        mockMvc.perform(get("/api/v1/studies/{studyId}/todos", study.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }
}
