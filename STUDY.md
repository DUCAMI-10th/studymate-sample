# 5. ApiResponse

## 이 브랜치의 목표

API마다 제각각이던 응답 형식을 `ApiResponse` 로 통일하는 단계입니다.

## 여기서 배우는 것

- 공통 응답 래퍼가 필요한 이유
- `status`, `message`, `data` 같은 필드를 한 번에 관리하는 방식
- 컨트롤러에서 응답 코드를 더 일관되게 표현하는 방법

## 지금 코드에서 볼 파일

- `src/main/java/com/ducami/studymate/global/data/ApiResponse.java`
- `src/main/java/com/ducami/studymate/domain/study/controller/StudyController.java`

## 왜 필요한가

조회 API는 JSON을 주고, 생성 API는 본문이 없고, 수정 API는 또 다른 형식을 주기 시작하면 프론트엔드와 테스트 코드가 점점 복잡해집니다. 공통 응답 형식은 백엔드와 프론트엔드가 대화하는 규칙을 만드는 작업입니다.

## 아직 아쉬운 점

- 에러 응답 형식은 아직 충분히 정리되지 않았다.
- DTO 보일러플레이트가 계속 늘어난다.

## 다음 브랜치에서 할 것

다음 단계에서는 DTO를 `record` 로 정리하고, 예외를 전역에서 처리하는 방향으로 리팩토링합니다.
