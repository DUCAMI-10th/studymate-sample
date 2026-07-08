# Chapter 06. JWT와 인증된 사용자

## 이번 챕터에서 만들 기능

이번 챕터에서는 로그인 결과로 JWT를 발급하고, 이후 요청에서 토큰을 검사해 현재 사용자를 확인합니다.

- 로그인 성공 시 Access Token 발급
- `Authorization: Bearer <token>` 헤더 처리
- 인증이 필요한 API 보호
- Study 작성자 검증
- Todo 작성자 검증

회원가입과 로그인 검증은 이전 챕터에서 다루었습니다. 이번 챕터에서는 “로그인한 사용자가 다음 요청에서도 본인임을 어떻게 증명하는가”를 다룹니다.

## 먼저 확인할 파일

```text
src/main/java/com/ducami/studymate/global/security/
├── config/SecurityConfig.java
├── jwt/JwtProvider.java
├── jwt/filter/JwtAuthenticationFilter.java
└── principal/UserPrincipal.java

src/main/java/com/ducami/studymate/domain/auth/
├── controller/AuthController.java
├── dto/response/TokenResponse.java
└── service/AuthService.java
```

작성자 검증은 Study와 Todo Entity에서도 확인합니다.

```text
domain/study/entity/StudyEntity.java
domain/todo/entity/TodoEntity.java
```

## Bean과 DI 설명

`SecurityConfig`, `JwtAuthenticationFilter`, `JwtProvider`는 Spring Bean입니다.

`SecurityConfig`는 어떤 요청을 허용하고 어떤 요청을 막을지 설정합니다. `JwtAuthenticationFilter`는 요청이 Controller에 도착하기 전에 Authorization 헤더를 확인합니다.

```java
private final JwtAuthenticationFilter jwtAuthenticationFilter;
```

이 필터도 직접 생성하지 않습니다. Spring이 Bean으로 관리하고, `SecurityConfig`에서 필터 체인에 추가합니다.

## 로그인 후 토큰 발급 흐름

```text
클라이언트
-> POST /api/v1/auth/login
-> AuthController.login()
-> AuthService.login()
-> 이메일/비밀번호 검증
-> JwtProvider.generateToken(user)
-> TokenResponse
-> HTTP 200 OK
```

응답 예시:

```json
{
  "status": 200,
  "message": "로그인에 성공했습니다.",
  "data": {
    "accessToken": "eyJ..."
  }
}
```

JWT 안에는 사용자 id, email, role 같은 정보가 들어갑니다. 서버는 다음 요청에서 이 토큰을 읽어 현재 사용자를 다시 확인합니다.

## 인증 요청 흐름

클라이언트는 로그인 후 받은 토큰을 다음 요청 헤더에 넣습니다.

```text
Authorization: Bearer eyJ...
```

요청은 Controller에 바로 도착하지 않습니다. 먼저 `JwtAuthenticationFilter`를 거칩니다.

```text
클라이언트 요청
-> JwtAuthenticationFilter
-> Authorization 헤더에서 토큰 추출
-> JwtProvider로 토큰 검증
-> userId 추출
-> UserPrincipal 생성
-> SecurityContextHolder에 인증 정보 저장
-> Controller 실행
```

Controller에서는 다음처럼 현재 사용자를 받을 수 있습니다.

```java
@AuthenticationPrincipal UserPrincipal userPrincipal
```

이 값이 있어야 Study나 Todo를 누가 만들었는지 저장할 수 있습니다.

## SecurityConfig 흐름

`SecurityConfig`에서는 공개 API와 인증 API를 나눕니다.

```java
.requestMatchers(
        "/error",
        "/api/v1/users/signup",
        "/api/v1/auth/login"
).permitAll()
.requestMatchers(HttpMethod.GET, "/api/v1/studies/**").permitAll()
.anyRequest().authenticated()
```

이 설정의 의미는 다음과 같습니다.

- 회원가입과 로그인은 토큰 없이 접근할 수 있습니다.
- Study 조회는 토큰 없이 접근할 수 있습니다.
- 생성, 수정, 삭제는 인증이 필요합니다.

수업에서는 다음 요청을 직접 비교해 보는 것이 좋습니다.

1. 토큰 없이 `GET /api/v1/studies` 요청
2. 토큰 없이 `POST /api/v1/studies` 요청
3. 로그인 후 토큰을 붙여 `POST /api/v1/studies` 요청

## 작성자 검증

인증이 들어오면 Study와 Todo에 작성자 개념을 붙일 수 있습니다.

Study 수정/삭제에서는 owner를 확인합니다.

```java
public void validateOwner(Long ownerId) {
    if (!this.getOwner().getId().equals(ownerId)) {
        throw new StudyForbiddenException();
    }
}
```

Todo 수정/삭제/status 변경에서는 author를 확인합니다.

```java
public void validateAuthor(Long authorId) {
    if (!this.author.getId().equals(authorId)) {
        throw new TodoForbiddenException();
    }
}
```

이 메서드들은 과한 추상화가 아닙니다. Entity가 가진 소유권 규칙을 직접 표현합니다. `resolvePermission` 같은 범용 이름보다 `validateOwner`, `validateAuthor`가 훨씬 읽기 쉽습니다.

## 이번 챕터에서 제외할 내용

- React 연동
- Docker
- Refresh Token
- Logout
- Email verification
- UseCase 계층
- Domain Object와 Entity 분리
- `resolveUser`, `resolveToken` 같은 범용 이름의 helper
- `public create()`가 바로 `private createInternal()`만 호출하는 구조

Refresh Token과 Logout은 실제 서비스에서 중요하지만, 이 과정에서는 Access Token 하나로 인증 흐름을 이해하는 데 집중합니다.

## 수업 중 확인 질문

1. 회원가입과 로그인은 토큰 없이 접근 가능해야 하는 이유는 무엇인가요?
2. JWT는 어느 시점에 생성되고, 어느 시점에 검증되나요?
3. Controller 전에 Filter가 실행된다는 말은 어떤 의미인가요?
4. `@AuthenticationPrincipal`로 받은 사용자 id는 어디에서 사용되나요?
5. 작성자가 아닌 사용자가 수정하지 못하게 막는 코드는 어디에 있나요?
