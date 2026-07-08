# Chapter 01. Study 조회 흐름 읽기

## 이번 챕터에서 확인할 기능

이번 챕터에서는 Study 조회 API를 읽습니다.

- `GET /api/v1/studies`: 스터디 목록 조회
- `GET /api/v1/studies/{id}`: 스터디 상세 조회

이 브랜치에는 기본 생성, 수정, 삭제 코드도 포함되어 있습니다. 하지만 첫 수업에서는 조회 흐름만 먼저 다룹니다. 조회 흐름을 이해하면 이후 생성, 수정, 삭제 코드도 같은 구조 안에서 읽을 수 있습니다.

## 먼저 확인할 파일

```text
src/main/java/com/ducami/studymate/domain/study/
├── controller/StudyController.java
├── dto/StudyResponse.java
├── dto/StudySummaryResponse.java
├── entity/StudyEntity.java
├── repository/StudyRepository.java
└── service/StudyServiceImpl.java
```

처음에는 파일을 모두 자세히 읽지 않아도 됩니다. 요청이 들어왔을 때 어떤 순서로 이동하는지만 먼저 확인합니다.

## Bean 설명

Bean은 Spring이 만들고 관리하는 객체입니다.

다음 클래스들은 직접 `new`로 생성하지 않아도 Spring이 실행 중에 객체로 만들어 둡니다.

- `StudyController`
- `StudyServiceImpl`
- `StudyRepository`

예를 들어 `StudyController`에는 `@RestController`가 붙어 있습니다.

```java
@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;
}
```

`@RestController`가 붙은 클래스는 HTTP 요청을 처리하는 Bean으로 등록됩니다. 그래서 사용자가 `/api/v1/studies`로 요청을 보내면 Spring이 이 Controller를 찾아 메서드를 실행합니다.

## DI 설명

DI는 필요한 객체를 직접 생성하지 않고 주입받는 방식입니다.

`StudyController`는 `StudyService`가 필요합니다.

```java
private final StudyService studyService;
```

하지만 Controller 안에서 다음처럼 작성하지 않습니다.

```java
private final StudyService studyService = new StudyServiceImpl(...);
```

Service를 직접 만들면 Controller가 Service 생성 방법까지 알아야 합니다. 지금 구조에서는 Spring이 `StudyServiceImpl` Bean을 찾아 Controller에 넣어 줍니다.

이 덕분에 Controller는 다음 역할에 집중할 수 있습니다.

- 어떤 URL을 받을지 정합니다.
- 어떤 Service 메서드를 호출할지 정합니다.
- 어떤 HTTP 응답을 돌려줄지 정합니다.

## 목록 조회 흐름

```text
클라이언트
-> GET /api/v1/studies
-> StudyController.findAll()
-> StudyServiceImpl.findAll()
-> studyRepository.findAll()
-> List<StudyEntity>
-> List<StudySummaryResponse>
-> HTTP 200 OK
```

Controller 메서드는 다음 형태입니다.

```java
@GetMapping
public ResponseEntity<List<StudySummaryResponse>> findAll() {
    return ResponseEntity.ok(studyService.findAll());
}
```

여기서 확인할 부분은 두 가지입니다.

1. `@GetMapping`은 GET 요청과 메서드를 연결합니다.
2. Controller는 직접 DB를 조회하지 않고 `studyService.findAll()`을 호출합니다.

Service 메서드는 Repository를 사용합니다.

```java
public List<StudySummaryResponse> findAll() {
    return studyRepository.findAll().stream()
            .map(StudySummaryResponse::toEntity)
            .toList();
}
```

`studyRepository.findAll()`은 DB에 저장된 Study 전체를 조회합니다. 조회 결과는 `StudyEntity` 목록입니다. API 응답에는 Entity를 그대로 내보내지 않고 `StudySummaryResponse`로 바꿔서 돌려줍니다.

## 상세 조회 흐름

상세 조회는 id가 필요합니다.

```text
클라이언트
-> GET /api/v1/studies/1
-> StudyController.findById(1)
-> StudyServiceImpl.findById(1)
-> studyRepository.findById(1)
-> StudyEntity
-> StudyResponse
-> HTTP 200 OK
```

Controller에서는 URL의 `{id}` 값을 `@PathVariable`로 받습니다.

```java
@GetMapping("/{id}")
public ResponseEntity<StudyResponse> findById(@PathVariable Long id) {
    return ResponseEntity.ok(studyService.findById(id));
}
```

`/api/v1/studies/1`로 요청하면 `id`에는 `1`이 들어갑니다.

## 역할 분리 기준

각 클래스는 다음 기준으로 나눕니다.

| 구분 | 담당 |
| --- | --- |
| Controller | URL, HTTP 메서드, 요청값, 응답값 |
| Service | 작업 순서와 비즈니스 흐름 |
| Repository | DB 조회와 저장 |
| Entity | DB 테이블과 매핑되는 객체 |
| Response DTO | 클라이언트에게 보여 줄 응답 모양 |

Controller에서 Repository를 바로 호출할 수도 있습니다. 하지만 그렇게 작성하면 요청 처리 코드와 DB 접근 코드가 한 파일에 섞입니다. 뒤에서 생성, 수정, 삭제, validation, 인증이 추가되면 Controller가 너무 많은 책임을 가지게 됩니다. 그래서 첫 단계부터 역할을 분리해서 읽습니다.

## 실습 순서

1. 서버를 실행합니다.
2. `GET /api/v1/studies`를 호출합니다.
3. `StudyController.findAll()`에 중단점을 걸거나 코드를 따라 읽습니다.
4. `StudyServiceImpl.findAll()`에서 Repository 호출을 확인합니다.
5. `StudySummaryResponse`로 변환되는 지점을 확인합니다.
6. `GET /api/v1/studies/{id}`도 같은 방식으로 따라갑니다.

예시 응답 형태:

```json
[
  {
    "id": 1,
    "title": "스프링 스터디"
  }
]
```

상세 조회에서는 목록보다 더 많은 정보를 응답할 수 있습니다.

```json
{
  "id": 1,
  "title": "스프링 스터디",
  "content": "매주 월요일 진행합니다."
}
```

## 리팩토링 체크포인트

이번 챕터에서는 리팩토링을 거의 하지 않습니다. 먼저 요청 흐름을 읽는 것이 우선입니다.

허용되는 정리:

- 메서드 이름을 역할이 보이게 유지합니다.
- Controller에서 Repository를 직접 호출하지 않습니다.
- Entity를 응답으로 바로 내보내지 않고 Response DTO를 사용합니다.

이번 챕터에서 제외할 내용:

- React 연동
- Docker
- Refresh Token
- Logout
- Email verification
- UseCase 계층
- Domain Object와 Entity 분리
- `resolveStudy` 같은 흐린 이름의 범용 helper
- `public create()`가 바로 `private createInternal()`만 호출하는 구조

## 수업 중 확인 질문

1. `/api/v1/studies` 요청은 어떤 Controller 메서드로 연결되나요?
2. Controller가 Repository를 직접 호출하지 않는 이유를 역할 기준으로 설명할 수 있나요?
3. `StudyEntity`와 `StudySummaryResponse`는 어떤 차이가 있나요?
4. `@PathVariable Long id`에는 어떤 값이 들어가나요?

이 질문들에 답할 수 있으면 다음 챕터에서 생성, 수정, 삭제 흐름으로 넘어갑니다.
