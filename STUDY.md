# 1. Project Init

## 이 브랜치의 목표

스프링부트 프로젝트를 처음 생성하고, 서버가 실행되는 가장 작은 출발점을 이해하는 단계입니다.

## 여기서 배우는 것

- Gradle 기반 스프링부트 프로젝트 구조
- `main` 메서드에서 애플리케이션이 시작되는 방식
- `src/main` 과 `src/test` 의 기본 역할

## 지금 코드에서 볼 파일

- `build.gradle`
- `settings.gradle`
- `src/main/java/com/ducami/studymate/StudymateApplication.java`
- `src/main/resources/application.yaml`

## 아직 없는 것

- 도메인별 패키지 구조
- Controller, Service, Repository, Entity
- 입력 검증
- 공통 응답 형식
- 예외 처리

## 왜 이 단계가 중요한가

처음부터 기능을 많이 넣기보다, 프로젝트가 어떤 뼈대로 시작하는지 먼저 알아야 이후에 추가되는 코드의 위치와 역할을 이해할 수 있습니다.

## 다음 브랜치에서 할 것

다음 단계에서는 `Study` 도메인을 만들고 CRUD를 직접 구현하면서 백엔드의 기본 흐름인 `Controller -> Service -> Repository -> Entity` 를 처음 경험합니다.
