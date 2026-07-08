# Chapter 01. Study 조회 흐름 읽기

## 목표

이 챕터의 목표는 Spring Boot 서버에서 HTTP 요청이 들어와 DB 조회를 거쳐 응답으로 나가는 흐름을 읽는 것이다. 먼저 `GET /api/v1/studies`와 `GET /api/v1/studies/{id}`만 따라간다. 이 브랜치에는 기본 생성/수정/삭제 코드도 있지만, 수업에서는 조회 흐름을 먼저 설명한다.

## 핵심 개념

### Bean

Bean은 Spring이 대신 만들고 관리하는 객체다. `@RestController`, `@Service`, `@Repository`가 붙은 클래스는 Spring이 실행 시점에 객체로 만들어 둔다. 우리가 직접 `new StudyServiceImpl()`을 하지 않아도 Controller에서 Service를 사용할 수 있는 이유가 여기에 있다.

### DI

DI는 필요한 객체를 직접 만들지 않고 외부에서 주입받는 방식이다. 이 코드에서는 `@RequiredArgsConstructor`와 `final` 필드를 사용한다.

```java
private final StudyService studyService;
```

Controller는 Service가 어떻게 만들어지는지 몰라도 된다. 요청을 받는 역할에 집중하고, 실제 조회는 Service에 맡긴다.

## 현재 흐름

```text
HTTP GET 요청
-> StudyController
-> StudyService
-> StudyRepository
-> StudyEntity
-> StudyResponse
-> HTTP 응답
```

## 왜 이렇게 나누는가

- Controller는 URL, HTTP 메서드, 요청/응답을 담당한다.
- Service는 애플리케이션의 작업 순서를 담당한다.
- Repository는 DB 접근을 담당한다.
- Entity는 DB 테이블과 연결되는 데이터 구조다.
- Response DTO는 Entity를 그대로 노출하지 않기 위해 사용한다.

처음에는 이 분리 자체가 번거로워 보일 수 있다. 하지만 Controller가 DB 조회까지 직접 하면 코드가 커지고, 나중에 수정/삭제/검증 로직이 들어올 때 역할이 섞인다.

## 실습 순서

1. 서버를 실행한다.
2. H2 또는 API 호출로 Study 목록을 확인한다.
3. `StudyController.findAll()`에서 시작해 `StudyServiceImpl.findAll()`로 이동한다.
4. `studyRepository.findAll()`이 어떤 역할인지 확인한다.
5. `StudySummaryResponse`가 왜 필요한지 확인한다.

## 리팩토링 체크포인트

이 단계에서는 리팩토링을 거의 하지 않는다. 먼저 흐름을 읽는 것이 우선이다.

허용되는 정리:

- 메서드 이름을 역할이 보이게 유지한다.
- Controller에서 Repository를 직접 호출하지 않는다.

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

## 설명 포인트

학생에게 먼저 물어볼 질문은 하나다.

> 브라우저나 Postman에서 `/api/v1/studies`를 호출했을 때, 어떤 클래스들을 순서대로 지나갈까?

이 질문에 답할 수 있으면 다음 챕터로 넘어간다.
