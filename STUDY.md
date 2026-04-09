# 9. Todo Domain

## 이 브랜치의 목표

`Study` 와 연결되는 하위 도메인 `Todo` 를 추가해 도메인 관계와 기능 확장을 경험하는 단계입니다.

## Todo 도메인이란

`Todo` 는 스터디 안에서 해야 할 과제나 할 일을 나타내는 도메인입니다.

- 특정 `Study` 에 소속된다.
- 내용(`content`)과 상태(`status`)를 가진다.
- 목록 조회, 단건 조회, 생성, 수정, 상태 변경, 삭제를 지원한다.

## 여기서 배우는 것

- 상위 리소스 아래에 하위 리소스를 두는 URL 설계
- 연관관계 매핑
- 상태 변경 API 분리
- 도메인 확장 시 테스트가 왜 중요한지

## 지금 코드에서 볼 파일

- `src/main/java/com/ducami/studymate/domain/todo/controller/TodoController.java`
- `src/main/java/com/ducami/studymate/domain/todo/service/TodoServiceImpl.java`
- `src/main/java/com/ducami/studymate/domain/todo/entity/TodoEntity.java`
- `src/main/java/com/ducami/studymate/domain/todo/repository/TodoRepository.java`
- `src/test/java/com/ducami/studymate/domain/todo/controller/TodoControllerTest.java`

## 왜 필요한가

단일 도메인 CRUD만으로는 관계형 데이터 모델의 맛이 잘 드러나지 않습니다. `Todo` 를 붙이면 "상위 엔티티와 하위 엔티티가 어떻게 연결되는가", "상태 변경은 왜 별도 API로 둘 수 있는가" 같은 실전적인 고민이 시작됩니다.

## 다음 브랜치에서 할 것

다음 단계는 현재까지의 산출물을 하나로 모은 통합본을 보면서, 남은 과제인 Swagger, CORS, React 연결, Docker 배포를 어떻게 이어갈지 정리하는 단계입니다.
