# Chapter 05. 회원가입과 로그인

## 이번 챕터에서 만들 기능

이번 챕터에서는 사용자 계정을 만들고, 이메일과 비밀번호로 로그인하는 흐름을 구현합니다.

- `POST /api/v1/users/signup`: 회원가입
- `POST /api/v1/auth/login`: 로그인

JWT는 아직 만들지 않습니다. 이번 챕터에서는 사용자를 저장하고 비밀번호를 안전하게 비교하는 흐름에 집중합니다. 토큰 발급과 인증 필터는 다음 챕터에서 다룹니다.

## 먼저 확인할 파일

```text
src/main/java/com/ducami/studymate/domain/user/
├── controller/UserController.java
├── dto/request/SignupRequest.java
├── dto/response/SignupResponse.java
├── entity/UserEntity.java
├── repository/UserRepository.java
└── service/UserService.java

src/main/java/com/ducami/studymate/domain/auth/
├── controller/AuthController.java
├── dto/request/LoginRequest.java
└── service/AuthService.java
```

비밀번호 암호화 설정은 다음 파일에서 확인합니다.

```text
src/main/java/com/ducami/studymate/global/config/PasswordConfig.java
```

## Bean과 DI 설명

`UserService`은 `UserRepository`와 `PasswordEncoder`를 주입받습니다.

```java
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
```

`PasswordEncoder`는 직접 `new BCryptPasswordEncoder()`로 매번 만들지 않습니다. `PasswordConfig`에서 Bean으로 등록합니다.

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

이렇게 등록하면 회원가입 Service와 로그인 Service가 같은 방식으로 비밀번호를 암호화하고 비교할 수 있습니다.

`BCryptPasswordEncoder`는 같은 비밀번호라도 저장할 때마다 다른 암호화 문자열을 만들 수 있습니다. 그래서 로그인할 때는 저장된 값을 다시 평문으로 되돌리지 않고 `matches(입력값, 저장된값)`으로 비교합니다.

## 회원가입 흐름

```text
클라이언트
-> POST /api/v1/users/signup
-> UserController.signup()
-> UserService.signup()
-> 이메일 중복 확인
-> passwordEncoder.encode(...)
-> UserEntity.signup(...)
-> userRepository.save(user)
-> HTTP 201 Created
```

회원가입 요청 예시:

```json
{
  "name": "홍길동",
  "email": "study@example.com",
  "password": "password123"
}
```

비밀번호는 그대로 저장하지 않습니다.

```java
UserEntity user = UserEntity.signup(request, passwordEncoder.encode(request.getPassword()));
```

DB에는 암호화된 문자열이 저장됩니다. 로그인할 때도 원본 비밀번호를 복호화하지 않습니다. 입력된 비밀번호와 저장된 암호화 문자열을 `passwordEncoder.matches(...)`로 비교합니다.

## UserEntity.signup 기준

이번 챕터에서는 `UserEntity.signup(...)` 정적 메서드를 사용합니다.

```java
public static UserEntity signup(SignupRequest request, String encodedPassword) {
    return new UserEntity(
            request.getName(),
            request.getEmail(),
            encodedPassword,
            UserRole.USER
    );
}
```

이 메서드는 단순 wrapper가 아닙니다. 회원가입 시 기본 권한이 `USER`라는 규칙을 한 곳에 모읍니다. 이런 경우에는 정적 생성 메서드를 사용해도 좋습니다.

여기서는 Builder를 쓰지 않습니다. 필드가 많지 않고 기본 권한을 넣는 규칙만 보여 주면 되기 때문에 생성자 호출이 더 직접적입니다.

반대로 특별한 규칙이 없는 Entity까지 모두 `create()` 메서드로 감쌀 필요는 없습니다.

## 로그인 흐름

```text
클라이언트
-> POST /api/v1/auth/login
-> AuthController.login()
-> AuthService.login()
-> email로 User 조회
-> passwordEncoder.matches(...)
-> UserResponse 반환
```

로그인 요청 예시:

```json
{
  "email": "study@example.com",
  "password": "password123"
}
```

로그인은 저장이 아니라 검증입니다. 이메일이 없거나 비밀번호가 다르면 같은 예외를 사용합니다.

```java
throw new InvalidCredentialsException();
```

이렇게 처리하면 “이메일이 틀렸는지, 비밀번호가 틀렸는지”를 응답으로 구분하지 않습니다.

Service 코드에서는 먼저 이메일로 사용자를 찾고, 없으면 예외를 던집니다. 사용자가 있으면 그 다음에 비밀번호를 비교합니다. 이 순서를 나누어 읽으면 로그인 흐름이 단순해집니다.

## 이번 챕터에서 제외할 내용

- React 연동
- Docker
- Refresh Token
- Logout
- Email verification
- JWT
- SecurityConfig
- JwtAuthenticationFilter
- UseCase 계층
- Domain Object와 Entity 분리
- `resolveUser` 같은 범용 이름의 helper
- `public create()`가 바로 `private createInternal()`만 호출하는 구조

JWT를 제외하는 이유는 로그인 검증과 토큰 인증을 분리해서 이해하기 위해서입니다. 이번 챕터에서는 “사용자가 누구인지 확인하는 방법”까지만 다룹니다.

## 수업 중 확인 질문

1. 회원가입은 어떤 데이터를 DB에 저장하나요?
2. 비밀번호를 평문으로 저장하지 않는 이유는 무엇인가요?
3. `PasswordEncoder`를 Bean으로 등록하면 어떤 점이 편한가요?
4. 로그인은 데이터를 저장하는 API인가요, 검증하는 API인가요?
