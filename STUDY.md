# 3. Validation

## 이 브랜치의 목표

클라이언트가 잘못된 값을 보내도 서버가 무조건 저장하지 않도록 입력 검증을 추가하는 단계입니다.

## 여기서 배우는 것

- `spring-boot-starter-validation` 의 역할
- `@Valid` 가 Controller에서 동작하는 방식
- `@NotBlank` 같은 검증 어노테이션이 DTO에 붙는 이유

## 지금 코드에서 볼 파일

- `build.gradle`
- `src/main/java/com/ducami/studymate/domain/study/controller/StudyController.java`
- `src/main/java/com/ducami/studymate/domain/study/dto/request/CreateStudyRequest.java`

## 왜 필요한가

`title` 이 비어 있는 스터디가 저장되면, API는 동작하더라도 서비스 품질은 나빠집니다. 입력 검증은 "문제가 생긴 뒤 수정" 이 아니라 "문제가 들어오지 못하게 막는" 첫 번째 방어선입니다.

## 아직 아쉬운 점

- 검증 실패 시 응답 형식이 아직 일정하지 않다.
- 엔티티마다 공통으로 들어갈 필드는 반복될 수 있다.

## 다음 브랜치에서 할 것

다음 단계에서는 여러 엔티티에 공통으로 들어갈 생성 시간, 수정 시간 같은 필드를 `BaseEntity` 로 공통화합니다.
