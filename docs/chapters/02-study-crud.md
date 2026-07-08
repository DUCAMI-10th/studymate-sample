# Chapter 02. Study 생성, 수정, 삭제

## 목표

이 챕터에서는 Study 조회 흐름에 쓰기 기능을 붙인다. `POST`, `PUT`, `DELETE`가 각각 어떤 의도로 쓰이는지 보고, Request DTO와 Validation을 도입한다.

## 핵심 개념

### Bean

`StudyController`, `StudyServiceImpl`, `StudyRepository`는 모두 Spring Bean으로 관리된다. Controller는 Service Bean을 주입받고, Service는 Repository Bean을 주입받는다.

### DI

DI 덕분에 Controller는 Service 구현체를 직접 생성하지 않는다.

```java
private final StudyService studyService;
```

이 구조는 테스트와 변경에 유리하다. 지금은 단순하지만, 뒤에서 인증과 예외 처리가 붙어도 Controller의 기본 역할은 유지된다.

### Request DTO

생성 요청과 수정 요청은 Entity가 아니라 DTO로 받는다.

```java
public record CreateStudyRequest(
        @NotBlank(message = "제목은 필수입니다.")
        String title,
        String content
) {
}
```

Entity는 DB와 연결되는 객체이고, Request DTO는 HTTP 요청 데이터를 받는 객체다. 두 역할을 분리해야 API 입력이 바뀌어도 Entity를 덜 흔들 수 있다.

## 현재 흐름

```text
HTTP POST /api/v1/studies
-> StudyController.create()
-> StudyService.save()
-> new StudyEntity(request)
-> studyRepository.save(entity)
-> HTTP 201
```

수정 흐름은 조금 다르다.

```text
HTTP PUT /api/v1/studies/{id}
-> Study 조회
-> StudyEntity.update(request)
-> 트랜잭션 종료 시 변경 감지
```

## 왜 이렇게 설계하는가

- Controller는 HTTP 요청을 Java 객체로 받는다.
- Service는 어떤 Entity를 저장하거나 수정할지 결정한다.
- Entity는 자신의 값을 바꾸는 메서드를 가진다.
- Repository는 저장과 조회만 담당한다.

수정할 때 무조건 새 객체를 만들어 `save`하는 방식보다, 조회한 Entity의 값을 바꾸는 흐름을 먼저 보여주는 것이 JPA 변경 감지를 이해하기 좋다.

## 실습 순서

1. `POST /api/v1/studies`로 Study를 생성한다.
2. 빈 제목을 보내 validation 실패를 확인한다.
3. `PUT /api/v1/studies/{id}`로 제목과 내용을 수정한다.
4. `DELETE /api/v1/studies/{id}`로 삭제한다.
5. 생성, 수정, 삭제가 각각 Service에서 어떻게 다르게 처리되는지 비교한다.

## 리팩토링 체크포인트

허용되는 정리:

- Request DTO와 Response DTO 패키지를 나눈다.
- Entity에 `update` 메서드를 둔다.
- 제목 필수 같은 입력 검증은 DTO에 둔다.

아직 하지 않을 것:

- React 연동
- Docker
- Refresh Token
- Logout
- Email verification
- UseCase 계층
- Domain Object와 Entity 분리
- `resolveStudy` 같은 흐린 이름의 범용 helper
- 의미 없는 `public create()` -> `private createInternal()` wrapper

## 코드 스타일 원칙

이 정도는 충분히 단순하고 설명 가능하다.

```java
public StudyEntity(CreateStudyRequest request) {
    this.title = request.title();
    this.content = request.content();
}
```

다음 형태는 지금 단계에서 피한다.

```java
public static StudyEntity create(CreateStudyRequest request) {
    return createInternal(request);
}
```

실제 분기나 도메인 규칙이 없다면 wrapper는 학습 흐름을 흐린다.

## 설명 포인트

학생이 답해야 하는 질문은 다음이다.

> HTTP 요청으로 들어온 JSON은 어디에서 Java 객체가 되고, 그 객체는 언제 Entity로 바뀔까?
