# 2. Study CRUD

## 이 브랜치의 목표

프로젝트의 첫 도메인인 `Study` 를 만들고, 가장 기본적인 CRUD를 구현하는 단계입니다.

## Study 도메인이란

이 프로젝트에서 `Study` 는 스터디 게시글 또는 스터디 주제를 담는 가장 중심이 되는 도메인입니다.

- 제목(`title`)과 내용(`content`)을 가진다.
- 목록 조회, 단건 조회, 생성, 수정, 삭제가 가능하다.
- 이후 `Todo` 가 연결되는 기준이 되는 상위 도메인이다.

## 여기서 배우는 것

- URL 설계와 REST API 기본 형태
- Controller가 요청을 받고 Service에 위임하는 방식
- Entity와 Repository를 사용해 데이터를 저장하고 조회하는 방식
- Request DTO와 Response DTO를 나누는 이유

## 지금 코드에서 볼 파일

- `src/main/java/com/ducami/studymate/domain/study/controller/StudyController.java`
- `src/main/java/com/ducami/studymate/domain/study/service/StudyService.java`
- `src/main/java/com/ducami/studymate/domain/study/service/StudyServiceImpl.java`
- `src/main/java/com/ducami/studymate/domain/study/entity/StudyEntity.java`
- `src/main/java/com/ducami/studymate/domain/study/repository/StudyRepository.java`
- `src/main/java/com/ducami/studymate/domain/study/dto/request/CreateStudyRequest.java`
- `src/main/java/com/ducami/studymate/domain/study/dto/response/StudyResponse.java`

## 일부러 남겨둔 불편함

- 잘못된 입력이 들어와도 검증이 부족하다.
- 응답 형식이 API마다 단순 `ResponseEntity` 수준이다.
- 예외가 발생했을 때 공통된 방식으로 처리되지 않는다.

## 왜 다음 단계가 필요한가

CRUD만 먼저 만들면 빠르게 흐름은 잡히지만, 잘못된 요청을 막지 못합니다. 다음 브랜치에서는 `Validation` 을 넣어서 입력값을 더 안전하게 다룹니다.
