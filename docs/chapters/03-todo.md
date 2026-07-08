# Chapter 03. Study별 Todo 등록과 상태 변경

## 이번 챕터에서 만들 기능

이번 챕터에서는 Study 아래에 Todo를 추가합니다.

- `GET /api/v1/studies/{studyId}/todos`: 특정 Study의 Todo 목록 조회
- `POST /api/v1/studies/{studyId}/todos`: 특정 Study에 Todo 등록
- `PATCH /api/v1/studies/{studyId}/todos/{todoId}/status`: Todo 상태 변경

Todo는 혼자 존재하는 데이터가 아닙니다. 어느 Study에 속한 할 일인지 알아야 합니다. 그래서 URL에 `studyId`가 들어갑니다.

## 먼저 확인할 파일

```text
src/main/java/com/ducami/studymate/domain/todo/
├── controller/TodoController.java
├── dto/request/CreateTodoRequest.java
├── dto/request/UpdateTodoStatusRequest.java
├── dto/response/TodoResponse.java
├── entity/TodoEntity.java
├── enums/TodoStatus.java
├── repository/TodoRepository.java
└── service/TodoService.java
```

Study 쪽에서는 `StudyEntity`도 함께 봅니다. Todo가 Study와 연결되기 때문입니다.

## Bean과 DI 복습

`TodoController`, `TodoService`, `TodoRepository`, `StudyRepository`는 Spring Bean입니다.

`TodoService`은 두 Repository를 주입받습니다.

```java
private final TodoRepository todoRepository;
private final StudyRepository studyRepository;
```

Todo를 만들 때는 Todo 저장소만 있으면 부족합니다. 먼저 Study가 실제로 존재하는지 확인해야 하므로 `StudyRepository`도 필요합니다.

`TodoRepository`도 `JpaRepository<TodoEntity, Long>`을 상속합니다. 여기에 다음 메서드를 추가하면 Spring Data JPA가 이름을 보고 쿼리를 만듭니다.

```java
List<TodoEntity> findAllByStudyIdOrderByIdAsc(Long studyId);
```

이름은 길지만 다음처럼 나누어 읽으면 됩니다.

- `findAllByStudyId`: studyId가 같은 Todo를 모두 찾습니다.
- `OrderByIdAsc`: id 오름차순으로 정렬합니다.

## URL 구조

Todo API는 다음처럼 Study 아래에 배치합니다.

```text
/api/v1/studies/{studyId}/todos
```

이 URL은 다음 의미를 가집니다.

```text
studyId가 1인 Study의 Todo 목록
-> /api/v1/studies/1/todos
```

Controller에서는 `@PathVariable`로 `studyId`를 받습니다.

```java
@PostMapping
public ResponseEntity<Void> create(
        @PathVariable Long studyId,
        @RequestBody @Valid CreateTodoRequest request
) {
    todoService.save(studyId, request);
    return ResponseEntity.status(201).build();
}
```

## Todo 등록 흐름

```text
클라이언트
-> POST /api/v1/studies/{studyId}/todos
-> TodoController.create()
-> TodoService.save()
-> studyRepository.findById(studyId)
-> new TodoEntity(study, request)
-> todoRepository.save(todo)
-> HTTP 201 Created
```

중요한 지점은 Todo를 저장하기 전에 Study를 먼저 조회한다는 점입니다.

```java
StudyEntity study = findStudyOrThrow(studyId);
TodoEntity todo = new TodoEntity(study, request);
return todoRepository.save(todo).getId();
```

존재하지 않는 Study에 Todo를 만들 수는 없습니다. 그래서 `studyId`로 Study를 먼저 찾습니다.

## Entity 관계

`TodoEntity`에는 Study가 들어 있습니다.

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "study_id", nullable = false)
private StudyEntity study;
```

여기서는 “Todo 여러 개가 Study 하나에 속한다” 정도로 이해하면 충분합니다.

```text
Study 1개
-> Todo 여러 개
```

반대로 `StudyEntity`에는 Todo 목록이 있습니다.

```java
@OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
private List<TodoEntity> todos = new ArrayList<>();
```

처음에는 관계 설정을 모두 외우지 않아도 됩니다. 중요한 것은 Todo 테이블에 `study_id`가 저장되어 어느 Study의 Todo인지 구분한다는 점입니다.

## 상태 변경 흐름

Todo 상태는 enum으로 표현합니다.

```java
public enum TodoStatus {
    PENDING,
    DONE
}
```

상태 변경 API는 전체 Todo 내용을 바꾸는 것이 아니라 status만 바꿉니다. 그래서 `PATCH`를 사용합니다.

```text
PATCH /api/v1/studies/{studyId}/todos/{todoId}/status
-> TodoController.updateStatus()
-> TodoService.updateStatus()
-> findTodoOrThrow(studyId, todoId)
-> todo.updateStatus(request.getStatus())
```

예시 요청:

```json
{
  "status": "DONE"
}
```

## helper 메서드 기준

이번 챕터에서는 `findStudyOrThrow`, `findTodoOrThrow` 같은 helper가 등장합니다.

```java
private StudyEntity findStudyOrThrow(Long studyId) {
    Optional<StudyEntity> studyOptional = studyRepository.findById(studyId);

    if (studyOptional.isEmpty()) {
        throw new IllegalArgumentException("스터디가 존재하지 않습니다.");
    }

    return studyOptional.get();
}
```

이 helper는 허용됩니다. 이름만 봐도 “Study를 찾고, 없으면 예외를 던진다”는 의미가 드러납니다.

`Optional<StudyEntity>`는 Study가 있을 수도 있고 없을 수도 있다는 뜻입니다. 처음에는 `orElseThrow(...)`처럼 짧은 문법보다 `if`로 비어 있는지 확인하는 코드가 흐름을 따라가기 쉽습니다.

반대로 다음 이름은 피합니다.

```java
private StudyEntity resolveStudy(Long studyId)
```

`resolve`는 조회인지, 생성인지, 검증인지 바로 알기 어렵습니다. 초급 과정에서는 동작이 드러나는 이름을 사용합니다.

## 이번 챕터에서 제외할 내용

- React 연동
- Docker
- Refresh Token
- Logout
- Email verification
- 회원가입/로그인
- JWT
- 작성자 검증
- UseCase 계층
- Domain Object와 Entity 분리
- `public create()`가 바로 `private createInternal()`만 호출하는 구조

작성자 검증은 JWT를 배운 뒤에 추가합니다. 지금은 Study와 Todo의 관계를 먼저 이해하는 것이 목표입니다.

## 수업 중 확인 질문

1. Todo URL에 `studyId`가 들어가는 이유는 무엇인가요?
2. Todo를 저장하기 전에 Study를 먼저 조회하는 이유는 무엇인가요?
3. `PENDING`, `DONE`을 문자열이 아니라 enum으로 둔 이유는 무엇인가요?
4. Todo 상태 변경에 `PUT`이 아니라 `PATCH`를 사용한 이유를 설명할 수 있나요?
