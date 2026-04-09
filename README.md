# StudyMate Sample

처음 스프링부트 백엔드를 배우는 학생이 `Controller -> Service -> Entity -> Repository` 흐름을 따라가며 읽을 수 있도록 정리한 샘플입니다.

## 1. 먼저 어디부터 읽으면 좋은가

1. `src/main/java/com/ducami/studymate/StudymateApplication.java`
2. `src/main/java/com/ducami/studymate/domain/study/controller/StudyController.java`
3. `src/main/java/com/ducami/studymate/domain/study/service/StudyServiceImpl.java`
4. `src/main/java/com/ducami/studymate/domain/study/entity/StudyEntity.java`
5. `src/main/java/com/ducami/studymate/domain/todo/controller/TodoController.java`
6. `src/main/java/com/ducami/studymate/global/exception/handler/GlobalExceptionHandler.java`
7. `src/main/java/com/ducami/studymate/global/security/config/SecurityConfig.java`
8. `src/main/java/com/ducami/studymate/global/security/jwt/filter/JwtAuthenticationFilter.java`

처음에는 `study` 도메인의 CRUD부터 보고, 그 다음에 `todo`, 마지막으로 `security`를 읽는 순서를 권장합니다.

## 2. 이 프로젝트에서 보면 좋은 핵심 포인트

- `Controller`: HTTP 요청을 받고 응답을 돌려준다.
- `Service`: 실제 비즈니스 로직을 처리한다.
- `Entity`: 데이터베이스 테이블과 연결되는 객체다.
- `Repository`: JPA로 DB 접근을 담당한다.
- `Security`: 로그인 후 받은 JWT를 검사해서 현재 사용자를 확인한다.
- `GlobalExceptionHandler`: 예외를 한 곳에서 공통 응답으로 바꿔 준다.

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

1. 회원가입 `POST /api/v1/users/signup`
2. 로그인 `POST /api/v1/auth/login`
3. 스터디 목록 조회 `GET /api/v1/studies`
4. 스터디 생성 `POST /api/v1/studies`
5. Todo 생성 `POST /api/v1/studies/{studyId}/todos`
6. Todo 상태 변경 `PATCH /api/v1/studies/{studyId}/todos/{todoId}/status`

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
