# Travel App - 나만의 맞춤형 여행 일정 관리 앱

## 프로젝트 개요 (Overview)

- 여행 어플리케이션인 '트리플' 앱을 벤치마킹 해서 제작한 여행 장소, 경로, 일정을 직관적으로 관리할 수 있는 웹 애플리케이션입니다.
- 개발 인원: 1인(Full-Stack)
- 개발 기간: 2025.06.18 ~

## 핵심 기능 (Key Feature)

- 장소 및 일정: 위경도 데이터를 활용한 랜드마크 조회 및 일정 시퀀스(순서) 관리
- 배낭(Backpack): 여행 일정에 담긴(찜) 장소 한눈에 보기 기능.
- 인증(Auth): JWT 기반의 토큰 기반 로그인
- AI 기반 맞춤형 일정 추천: Gemini API를 연동하여 사용자의 취향에 맞는 최적의 여행 동선 자동 생성

## 보안 및 아키텍쳐 (Security & Architecture)

- 자격 증명 분리: 외부 API 키 및 DB 계정정보는 application-secret.properties로 분리하여 퍼블릭 노출 원천 차단
- 인증/인가: Spring Security와 JWT를 활용한 Stateless 사용자 인증 구현

### 기술

- **프레임워크:** Spring Boot
- **언어:** Java 17
- **빌드 툴:** Gradle
- **데이터베이스:** MariaDB (JDBC Driver)
- **ORM:** Spring Data JPA
- **템플릿 엔진:** Thymeleaf
- **유틸리티:** Lombok
- **시큐리티:** Spring Security
- **Web:** Spring Web
- **Validation:** Spring Boot Starter Validation

### 실행 방법

1.  `travel_app_backend` 디렉토리로 이동합니다:
    ```bash
    cd travel_app_backend
    ```
2.  Gradle을 사용하여 빌드합니다:
    ```bash
    ./gradlew clean build
    ```
3.  어플리케이션을 실행합니다:
    ```bash
    ./gradlew bootRun
    ```
    또한, 빌드가 끝난 후에는 JAR 파일로도 실행할 수 있습니다.:
    ```bash
    java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
    ```
    어플리케이션은 주로 `http://localhost:8080`에서 실행됩니다.

### Database Configuration

어플리케이션이 MariaDB를 사용하도록 구성되었습니다. MariaDB 인스턴스가 실행 중인지 확인 후, 필요 시 `travel_app_backend/src/main/resources/application.properties`에서 데이터베스 연결 속성을 업데이트합니다.

`application.properties`에 들어갈 예시입니다:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/travel_app_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update # or create, create-drop, none
spring.jpa.show-sql=true
```
