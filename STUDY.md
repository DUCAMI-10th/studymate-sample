# 6. Record And Exception

## 이 브랜치의 목표

DTO를 `record` 로 바꾸어 보일러플레이트를 줄이고, 예외 처리 구조를 공통화하는 단계입니다.

## 여기서 배우는 것

- DTO를 `record` 로 표현하는 이유
- 커스텀 예외와 상태 코드 분리
- `@RestControllerAdvice` 를 이용한 전역 예외 처리
- 성공 응답과 실패 응답을 같은 규칙 안에서 다루는 방식

## 지금 코드에서 볼 파일

- `src/main/java/com/ducami/studymate/domain/study/dto/request/CreateStudyRequest.java`
- `src/main/java/com/ducami/studymate/domain/study/dto/response/StudyResponse.java`
- `src/main/java/com/ducami/studymate/global/data/ErrorResponse.java`
- `src/main/java/com/ducami/studymate/global/exception/GlobalExceptionHandler.java`
- `src/main/java/com/ducami/studymate/domain/study/exception/StudyStatusCode.java`

## 왜 필요한가

DTO 클래스가 많아질수록 getter, constructor, equals 같은 코드가 반복됩니다. 또 예외를 컨트롤러마다 처리하기 시작하면 중복이 빠르게 늘어납니다. 이 단계는 "코드는 돌아가지만 읽기 어려운 상태" 에서 "규칙이 보이는 상태" 로 가기 위한 리팩토링입니다.

## 아직 아쉬운 점

- 사용자 도메인과 로그인 기능은 아직 없다.
- 인증이 필요한 API와 아닌 API의 구분이 없다.

## 다음 브랜치에서 할 것

다음 단계에서는 회원가입과 로그인 기능을 추가해 실제 사용자 흐름을 만들기 시작합니다.
