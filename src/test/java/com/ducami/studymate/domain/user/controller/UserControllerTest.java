package com.ducami.studymate.domain.user.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    @DisplayName("로그인에 성공하면 access token과 refresh token을 발급한다")
    void login() throws Exception {
        userRepository.save(UserEntity.builder()
                .name("tester")
                .email("tester@example.com")
                .password(passwordEncoder.encode("password123"))
                .role(UserRole.USER)
                .build());

        String request = """
                {
                  "email": "tester@example.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", is("로그인에 성공했습니다.")))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("리프레시 토큰으로 토큰을 재발급한다")
    void refresh() throws Exception {
        userRepository.save(UserEntity.builder()
                .name("tester")
                .email("tester@example.com")
                .password(passwordEncoder.encode("password123"))
                .role(UserRole.USER)
                .build());

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "tester@example.com",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String refreshToken = loginResult.getResponse()
                .getContentAsString()
                .split("\"refreshToken\":\"")[1]
                .split("\"")[0];

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "%s"
                                }
                                """.formatted(refreshToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", is("토큰을 재발급했습니다.")))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("인증 없이도 스터디 API에 접근할 수 있다")
    void accessWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/studies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)));
    }

    @Test
    @DisplayName("Access token으로도 스터디 API에 접근할 수 있다")
    void accessWithToken() throws Exception {
        userRepository.save(UserEntity.builder()
                .name("tester")
                .email("tester@example.com")
                .password(passwordEncoder.encode("password123"))
                .role(UserRole.USER)
                .build());

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "tester@example.com",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = loginResult.getResponse()
                .getContentAsString()
                .split("\"accessToken\":\"")[1]
                .split("\"")[0];

        mockMvc.perform(get("/api/v1/studies")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)));
    }
}
