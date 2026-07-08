# StudyMate Sample

처음 스프링부트 백엔드를 배우는 학생이 `Controller -> Service -> Entity -> Repository` 흐름을 따라가며 읽을 수 있도록 정리한 샘플입니다.

## 1. 먼저 어디부터 읽으면 좋은가

완성된 `main` 코드를 바로 읽으면 JWT, Security, 예외 처리까지 한 번에 보입니다. 처음 배우는 학생은 아래 브랜치 순서로 읽는 편이 좋습니다.

1. `chapter/01-study-read`: Study 조회 흐름
2. `chapter/02-study-crud`: Study 생성, 수정, 삭제
3. `chapter/03-todo`: Study별 Todo
4. `chapter/04-response-exception`: 공통 응답과 예외 처리
5. `chapter/05-auth-login`: 회원가입과 로그인
6. `chapter/06-jwt-security`: JWT와 Spring Security

완성본을 읽을 때는 다음 파일 순서를 권장합니다.

1. `src/main/java/com/ducami/studymate/domain/study/controller/StudyController.java`
2. `src/main/java/com/ducami/studymate/domain/study/service/StudyService.java`
3. `src/main/java/com/ducami/studymate/domain/study/repository/StudyRepository.java`
4. `src/main/java/com/ducami/studymate/domain/todo/controller/TodoController.java`
5. `src/main/java/com/ducami/studymate/global/exception/handler/GlobalExceptionHandler.java`
6. `src/main/java/com/ducami/studymate/global/security/config/SecurityConfig.java`
7. `src/main/java/com/ducami/studymate/global/security/jwt/filter/JwtAuthenticationFilter.java`

## 2. 이 프로젝트에서 보면 좋은 핵심 포인트

- `Controller`: HTTP 요청을 받고 응답을 돌려준다.
- `Service`: 실제 비즈니스 로직을 처리한다.
- `Entity`: 데이터베이스 테이블과 연결되는 객체다.
- `Repository`: JPA로 DB 접근을 담당한다.
- `Security`: 로그인 후 받은 JWT를 검사해서 현재 사용자를 확인한다.
- `GlobalExceptionHandler`: 예외를 한 곳에서 공통 응답으로 바꿔 준다.

처음 볼 때 헷갈리기 쉬운 문법:

- `record`: 값을 담는 DTO를 짧게 쓰는 Java 문법입니다. 생성자와 getter 역할의 메서드가 자동으로 생깁니다.
- `Optional<T>`: 값이 있을 수도 있고 없을 수도 있다는 뜻입니다. 초반 챕터에서는 `isEmpty()`와 `get()`으로 흐름을 풀어 씁니다.
- `@RequiredArgsConstructor`: `final` 필드를 받는 생성자를 Lombok이 대신 만들어 줍니다. 이 생성자를 통해 Spring이 Bean을 주입합니다.
- `session -> ...`: 람다입니다. Spring Security가 넘겨준 설정 객체를 오른쪽 코드에서 사용한다고 읽습니다.
- `AbstractHttpConfigurer::disable`: 메서드 참조입니다. Security 설정에서 해당 기능을 끈다고 읽으면 됩니다.

## 3. 실행 방법

기본 설정은 MySQL 기준입니다. 로컬 MySQL에 `studymate` 데이터베이스가 준비되어 있다면 바로 실행할 수 있습니다.

```bash
./gradlew bootRun
```

실행 후 확인할 수 있는 주소:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

환경변수를 따로 주고 싶다면 아래 값을 덮어쓰면 됩니다.

```bash
export DB_URL=jdbc:mysql://localhost:3306/studymate
export DB_USERNAME=mates
export DB_PASSWORD='Mates123!@#'
```

## 4. 추천 학습 순서

1. 스터디 목록 조회 `GET /api/v1/studies`
2. 스터디 상세 조회 `GET /api/v1/studies/{id}`
3. 회원가입 `POST /api/v1/users/signup`
4. 로그인 `POST /api/v1/auth/login`
5. 로그인 응답의 JWT를 `Authorization: Bearer <token>` 헤더에 넣기
6. 스터디 생성 `POST /api/v1/studies`
7. Todo 생성 `POST /api/v1/studies/{studyId}/todos`
8. Todo 상태 변경 `PATCH /api/v1/studies/{studyId}/todos/{todoId}/status`

## 5. 응답 형식

모든 응답은 아래 구조를 사용합니다.

```json
{
  "status": 200,
  "message": "요청에 성공했습니다.",
  "data": {}
}
```

성공/실패 모두 같은 모양을 유지해서, 프론트엔드나 테스트 코드에서 확인하기 쉽게 만들었습니다.
