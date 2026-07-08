# Chapter 02. Study 생성, 수정, 삭제

## 이번 챕터에서 만들 기능

이번 챕터에서는 Study 조회 API에 생성, 수정, 삭제 기능을 추가합니다.

- `POST /api/v1/studies`: 스터디 생성
- `PUT /api/v1/studies/{id}`: 스터디 수정
- `DELETE /api/v1/studies/{id}`: 스터디 삭제

조회 API는 서버가 DB에서 데이터를 꺼내 응답하는 흐름입니다. 생성, 수정, 삭제 API는 요청 데이터가 DB 상태를 바꾸는 흐름입니다. 그래서 이번 챕터에서는 `@RequestBody`, Request DTO, `@Transactional`, Validation을 함께 봅니다.

## 먼저 확인할 파일

```text
src/main/java/com/ducami/studymate/domain/study/
├── controller/StudyController.java
├── dto/request/CreateStudyRequest.java
├── dto/request/UpdateStudyRequest.java
├── entity/StudyEntity.java
├── repository/StudyRepository.java
└── service/StudyServiceImpl.java
```

파일을 읽을 때는 Controller부터 시작하는 편이 좋습니다. 실제 HTTP 요청이 Controller 메서드에 먼저 도착하기 때문입니다.

## Bean과 DI 복습

`StudyController`, `StudyServiceImpl`, `StudyRepository`는 Spring이 관리하는 객체입니다. 이런 객체를 Bean이라고 부릅니다.

Controller 안에는 다음 필드가 있습니다.

```java
private final StudyService studyService;
```

이 필드에 들어갈 객체를 직접 `new StudyServiceImpl(...)`로 만들지 않습니다. Spring이 실행 중에 필요한 객체를 넣어 줍니다. 이 방식을 DI라고 합니다. DI를 사용하면 Controller는 HTTP 요청 처리에 집중하고, Service 생성 방식은 Spring에게 맡길 수 있습니다.

## 생성 요청 흐름

스터디 생성 요청은 다음 순서로 처리됩니다.

```text
클라이언트
-> POST /api/v1/studies
-> StudyController.create()
-> CreateStudyRequest
-> StudyServiceImpl.save()
-> new StudyEntity(request)
-> studyRepository.save(entity)
-> HTTP 201 Created
```

Controller에서는 JSON 요청 본문을 Java 객체로 받습니다.

```java
public ResponseEntity<Void> create(@RequestBody CreateStudyRequest request) {
    studyService.save(request);
    return ResponseEntity.status(201).build();
}
```

`@RequestBody`는 HTTP 요청 본문에 들어 있는 JSON을 `CreateStudyRequest` 객체로 바꿔 줍니다. 이때 필드 이름이 맞아야 합니다.

예시 요청:

```json
{
  "title": "스프링 스터디",
  "content": "매주 월요일 진행합니다."
}
```

## Request DTO를 사용하는 기준

생성 요청은 Entity로 바로 받지 않고 Request DTO로 받습니다.

```java
public record CreateStudyRequest(
        @NotBlank(message = "제목은 필수입니다.")
        String title,
        String content
) {
}
```

DTO는 API 입력 형식을 표현합니다. Entity는 DB 테이블과 연결되는 객체입니다. 두 객체를 분리하면 API 요청 형식과 DB 구조를 따로 설명할 수 있습니다.

예를 들어 나중에 API 요청에는 `tagNames`가 필요하지만 DB에는 다른 테이블로 저장해야 할 수 있습니다. 이때 Request DTO와 Entity가 분리되어 있으면 변경 범위가 줄어듭니다.

## Validation 흐름

`@NotBlank`는 빈 문자열을 막습니다.

```java
@NotBlank(message = "제목은 필수입니다.")
String title
```

Controller 메서드에 `@Valid`가 붙으면 Spring은 Service를 호출하기 전에 DTO의 validation 규칙을 검사합니다.

```java
public ResponseEntity<Void> create(@RequestBody @Valid CreateStudyRequest request)
```

수업에서는 일부러 다음 요청을 보내 보시면 좋습니다.

```json
{
  "title": "",
  "content": "제목이 없는 요청입니다."
}
```

이 요청은 Service까지 가지 않고 Controller 앞단에서 실패합니다. 이 지점을 확인해야 validation의 위치를 이해할 수 있습니다.

## 수정 요청 흐름

수정은 생성과 다릅니다. 새 Entity를 만드는 것이 아니라 기존 Entity를 조회한 뒤 값을 바꿉니다.

```text
클라이언트
-> PUT /api/v1/studies/{id}
-> StudyController.update()
-> StudyServiceImpl.update()
-> studyRepository.findById(id)
-> study.update(request)
-> 트랜잭션 종료 시점에 DB 반영
```

Entity에는 값을 바꾸는 메서드가 있습니다.

```java
public void update(UpdateStudyRequest request) {
    if (request.title() != null) this.title = request.title();
    if (request.content() != null) this.content = request.content();
}
```

이 메서드는 간단하지만 중요한 역할을 합니다. 외부 코드가 Entity 필드를 직접 바꾸지 않고, Entity가 제공하는 메서드를 통해 변경합니다.

## 삭제 요청 흐름

삭제는 Controller에서 id를 받고 Service에서 Repository를 호출합니다.

```text
DELETE /api/v1/studies/{id}
-> StudyController.delete()
-> StudyServiceImpl.delete()
-> studyRepository.deleteById(id)
```

처음에는 이렇게 단순하게 시작해도 됩니다. 뒤에서 예외 처리 챕터를 진행할 때 존재하지 않는 id를 삭제하면 어떤 응답을 줄지 정리합니다.

## 코드 작성 기준

이번 챕터에서는 흐름이 보이는 코드가 가장 좋습니다.

좋은 예시:

```java
public StudyEntity(CreateStudyRequest request) {
    this.title = request.title();
    this.content = request.content();
}
```

현재 단계에서는 다음 구조를 만들지 않습니다.

```java
public static StudyEntity create(CreateStudyRequest request) {
    return createInternal(request);
}
```

생성 경로가 하나뿐이고 특별한 규칙도 없다면 한 번 더 감싸는 메서드는 필요하지 않습니다. 학생들이 실제 값이 어디에서 들어가는지 바로 볼 수 있어야 합니다.

## 이번 챕터에서 제외할 내용

- React 연동
- Docker
- Refresh Token
- Logout
- Email verification
- UseCase 계층
- Domain Object와 Entity 분리
- `resolveStudy` 같은 범용 이름의 helper
- `public create()`가 바로 `private createInternal()`만 호출하는 구조

## 수업 중 확인 질문

1. JSON 요청 본문은 어느 시점에 Java 객체가 되나요?
2. `CreateStudyRequest`와 `StudyEntity`는 각각 어떤 책임을 가지나요?
3. 생성은 `new StudyEntity(...)`를 사용하는데, 수정은 기존 Entity를 조회하는 이유는 무엇인가요?
4. `@Valid`가 실패하면 Service 메서드는 실행될까요?
