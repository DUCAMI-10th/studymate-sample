# 7. User Auth Basic

## 이 브랜치의 목표

회원가입과 로그인 기능을 추가해 "누가 요청했는가" 를 다루기 시작하는 단계입니다.

## 여기서 배우는 것

- `User` 도메인 설계
- 회원가입과 로그인 서비스 분리
- 비밀번호 암호화
- 인증 관련 예외 처리
- 보안 설정이 애플리케이션에 들어오는 방식

## 지금 코드에서 볼 파일

- `src/main/java/com/ducami/studymate/domain/user/controller/UserController.java`
- `src/main/java/com/ducami/studymate/domain/user/service/UserServiceImpl.java`
- `src/main/java/com/ducami/studymate/domain/auth/controller/AuthController.java`
- `src/main/java/com/ducami/studymate/domain/auth/service/AuthServiceImpl.java`
- `src/main/java/com/ducami/studymate/global/security/config/SecurityConfig.java`

## 읽을 때 주의할 점

이 브랜치에는 JWT 관련 코드도 함께 들어가 있습니다. 다만 학습 포인트는 "인증을 도입하면 어떤 도메인과 서비스가 추가되는가" 를 이해하는 데 두는 것이 좋습니다.

## 왜 필요한가

이전까지는 모든 요청이 익명 사용자 기준이었습니다. 하지만 실제 서비스에서는 "누가 생성했는지", "누가 수정할 수 있는지" 같은 규칙이 필요합니다. 사용자 도메인이 들어오면 도메인 간 관계와 권한 개념이 함께 시작됩니다.

## 다음 브랜치에서 할 것

다음 단계에서는 메모리 DB나 기본 설정 수준을 넘어서 실제 MySQL 연결을 다루며 실행 환경을 현실적으로 맞춥니다.
