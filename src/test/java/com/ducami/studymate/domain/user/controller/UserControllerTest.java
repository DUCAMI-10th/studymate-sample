package com.ducami.studymate.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입에 성공한다")
    void signup() throws Exception {
        String request = """
                {
                  "name": "tester",
                  "email": "tester@example.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is(201)))
                .andExpect(jsonPath("$.message", is("회원가입에 성공했습니다.")))
                .andExpect(jsonPath("$.data.name", is("tester")))
                .andExpect(jsonPath("$.data.email", is("tester@example.com")))
                .andExpect(jsonPath("$.data.role", is("USER")));
    }

    @Test
    @DisplayName("중복 이메일로 회원가입하면 예외가 발생한다")
    void signupWithDuplicateEmail() throws Exception {
        userRepository.save(UserEntity.builder()
                .name("existing")
                .email("tester@example.com")
                .password(passwordEncoder.encode("password123"))
                .role(UserRole.USER)
                .build());

        String request = """
                {
                  "name": "tester",
                  "email": "tester@example.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message", is("이미 사용 중인 이메일입니다.")))
                .andExpect(jsonPath("$.data.code", is("USER_409")));
    }
}
