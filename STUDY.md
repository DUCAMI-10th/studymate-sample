# 4. Base Entity

## 이 브랜치의 목표

여러 엔티티에서 반복될 수 있는 공통 필드를 `BaseEntity` 로 분리하는 단계입니다.

## 여기서 배우는 것

- JPA Auditing
- `@MappedSuperclass` 의 역할
- 공통 관심사를 상위 클래스로 올리는 리팩토링

## 지금 코드에서 볼 파일

- `src/main/java/com/ducami/studymate/StudymateApplication.java`
- `src/main/java/com/ducami/studymate/global/entity/BaseEntity.java`
- `src/main/java/com/ducami/studymate/domain/study/entity/StudyEntity.java`

## 왜 필요한가

`createdAt`, `updatedAt` 같은 필드를 엔티티마다 직접 작성하면 코드가 반복되고 관리 포인트가 늘어납니다. 공통 엔티티는 중복을 줄이고, 도메인 클래스가 본래 책임에 더 집중하게 도와줍니다.

## 아직 아쉬운 점

- 응답 형식이 아직 통일되지 않았다.
- 예외가 생기면 어디서 어떤 응답을 줄지 정해져 있지 않다.

## 다음 브랜치에서 할 것

다음 단계에서는 공통 응답 객체를 도입해서 API 응답 모양을 통일합니다.
