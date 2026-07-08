# Chapter 04. 공통 응답과 예외 처리

## 이번 챕터에서 정리할 내용

이번 챕터에서는 API 응답 모양과 예외 응답을 정리합니다.

이전 챕터까지는 Controller마다 `ResponseEntity.ok(...)`, `ResponseEntity.status(201).build()`를 직접 작성했습니다. 또한 존재하지 않는 Study나 Todo를 찾을 때 `IllegalArgumentException`을 사용했습니다.

이번 챕터에서는 다음 내용을 추가합니다.

- `ApiResponse`
- `ErrorResponse`
- `ApplicationException`
- 도메인별 예외
- `GlobalExceptionHandler`

## 먼저 확인할 파일

```text
src/main/java/com/ducami/studymate/global/
├── data/ApiResponse.java
├── data/ErrorResponse.java
└── exception/
    ├── ApplicationException.java
    ├── handler/GlobalExceptionHandler.java
    └── status/StatusCode.java
```

도메인 예외는 각 도메인 패키지 안에 둡니다.

```text
domain/study/exception/StudyNotFoundException.java
domain/todo/exception/TodoNotFoundException.java
```

## Bean과 DI 복습

`GlobalExceptionHandler`에는 `@RestControllerAdvice`가 붙어 있습니다. 이 클래스도 Spring이 관리합니다.

Controller에서 예외가 발생하면 Spring은 `GlobalExceptionHandler`에 있는 `@ExceptionHandler` 메서드를 찾아 실행합니다.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleApplicationException(ApplicationException e) {
        return ApiResponse.error(e.getStatusCode());
    }
}
```

이 구조 덕분에 각 Controller에서 try-catch를 반복하지 않아도 됩니다.

## 응답 모양 통일

조회 성공 응답은 다음 형태로 통일합니다.

```json
{
  "status": 200,
  "message": "스터디 목록을 조회했습니다.",
  "data": [
    {
      "id": 1,
      "title": "스프링 스터디"
    }
  ]
}
```

Controller는 직접 응답 객체를 만들지 않고 `ApiResponse`의 정적 메서드를 사용합니다.

```java
@GetMapping
public ResponseEntity<ApiResponse<List<StudySummaryResponse>>> findAll() {
    return ApiResponse.ok("스터디 목록을 조회했습니다.", studyService.findAll());
}
```

여기서 Controller가 담당하는 일은 세 가지입니다.

1. URL과 HTTP 메서드를 받습니다.
2. Service를 호출합니다.
3. 응답 메시지를 정해서 반환합니다.

## 예외 처리 흐름

존재하지 않는 Study를 조회하면 Service에서 `StudyNotFoundException`을 던집니다.

```java
return studyRepository.findById(id)
        .map(StudyResponse::toEntity)
        .orElseThrow(StudyNotFoundException::new);
```

이 예외는 `ApplicationException`을 상속합니다.

```java
public class StudyNotFoundException extends ApplicationException {
    public StudyNotFoundException() {
        super(StudyStatusCode.STUDY_NOT_FOUND);
    }
}
```

예외가 발생하면 다음 순서로 처리됩니다.

```text
StudyService
-> StudyNotFoundException 발생
-> GlobalExceptionHandler
-> ApiResponse.error(...)
-> HTTP 404 응답
```

예시 응답:

```json
{
  "status": 404,
  "message": "스터디를 찾을 수 없습니다.",
  "data": {
    "code": "STUDY_404",
    "message": "스터디를 찾을 수 없습니다.",
    "timestamp": "2026-07-08T12:00:00"
  }
}
```

## Validation 실패 흐름

`@Valid`가 붙은 요청에서 제목이나 내용이 비어 있으면 `MethodArgumentNotValidException`이 발생합니다.

```text
잘못된 JSON 요청
-> Controller 진입 전 validation 실패
-> GlobalExceptionHandler.handleValidationException()
-> HTTP 400 응답
```

이 흐름을 확인하면 validation이 Service 내부 로직이 아니라 요청 검증 단계에서 처리된다는 점을 이해할 수 있습니다.

## 코드 작성 기준

이번 챕터에서는 예외 구조를 크게 만들지 않습니다. 필요한 예외만 만듭니다.

- `StudyNotFoundException`
- `TodoNotFoundException`
- Validation 실패 응답

지금 단계에서 모든 상황을 미리 상상해서 예외 클래스를 만들 필요는 없습니다.

허용되는 helper:

```java
private StudyEntity findStudyOrThrow(Long studyId) {
    return studyRepository.findById(studyId)
            .orElseThrow(StudyNotFoundException::new);
}
```

피할 helper:

```java
private StudyEntity resolveStudy(Long studyId)
```

이름만 보고 동작이 보이지 않는 helper는 수업 자료에서 사용하지 않습니다.

## 이번 챕터에서 제외할 내용

- React 연동
- Docker
- Refresh Token
- Logout
- Email verification
- 회원가입/로그인
- JWT
- UseCase 계층
- Domain Object와 Entity 분리
- `public create()`가 바로 `private createInternal()`만 호출하는 구조

## 수업 중 확인 질문

1. 성공 응답과 실패 응답의 모양을 통일하면 어떤 점이 편해지나요?
2. Controller마다 try-catch를 쓰지 않아도 되는 이유는 무엇인가요?
3. `StudyNotFoundException`이 HTTP 404로 바뀌는 흐름을 설명할 수 있나요?
4. Validation 실패는 Service까지 도달할까요?
