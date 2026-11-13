# ⚙️ Travel App (Backend API Engine)

<div align="center">

**트리플(Triple) 스타일 맞춤형 여행 앱의 핵심 REST API 서버**
실시간 스카이스캐너 항공권 연동, Gemini AI 맞춤 코스 생성, 인터랙티브 플래너 시퀀스 관리,Stateless JWT 보안 인증

  <p align="center">
    <img src="https://img.shields.io/badge/Language-Java%2017-ED8B00?logo=openjdk&logoColor=white" alt="Java 17" />
    <img src="https://img.shields.io/badge/Framework-Spring%20Boot%203.5.0-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot 3.5" />
    <img src="https://img.shields.io/badge/ORM-Spring%20Data%20JPA-6DB33F?logo=hibernate&logoColor=white" alt="Spring Data JPA" />
    <img src="https://img.shields.io/badge/Database-MariaDB-003545?logo=mariadb&logoColor=white"alt="MariaDB" />
    <img src="https://img.shields.io/badge/Security-Spring%20Security%20%7C%20JWT-000000?logo=jsonwebtokens&logoColor=white" alt="JWT" />
    <img src="https://img.shields.io/badge/Build-Gradle-02303A?logo=gradle&logoColor=white"alt="Gradle" />
  </p>

🔗 **[📱 프론트엔드 레포지토리 바로가기 (travel_app_front)](https://github.com/junha2020/travel_app_front)**

  </div>

---

## 📌 1. 프로젝트 개요 (Overview)

- **레포지토리**: `travel_app` (Backend)
- **개발 인원**: 1인 개발 (김준하)
- **개발 기간**: 2025.06.18 ~ 2026.09.30
- **핵심 역할**:
  - 스카이스캐너 RapidAPI(`POST /api/v1/roundtrip`) 연동 및 한-일 왕복 항공권 실시간 정규화
  - Google Gemini API 연동을 통한 테마별 여행 동선 자동 빌드
  - 여행 플래너 드래그 앤 드롭 시퀀스 재정렬 및 일괄/개별 삭제 REST API
  - Spring Security & JWT 기반의 무상태(Stateless) 사용자 인증 및 데이터 보호

---

## 🛡️ 2. 보안 및 아키텍처 설계 (Security-First Architecture)

사이버보안 전공자의 시큐어 코딩 원칙에 기반하여 서버의 안정성과 보안성을 극대화했습니다.

1. **자격 증명 파일 완전 격리 (`application-secret.properties`)**:

- RapidAPI Key, Google Places/Gemini API Key, DB 패스워드, JWT 시크릿 등 모든 민감 정보를 `application-secret.properties`로 분리하고 `.gitignore`에 등록하여 **GitHub 퍼블릭 노출 가능성을 0%로 원천 차단**했습니다.

2. **무상태(Stateless) JWT 인증 파이프라인**:

- 세션 메모리 오버헤드가 없는 JWT(`io.jsonwebtoken:jjwt 0.12.3`) 토큰 기반 인증.
- `OncePerRequestFilter` 커스텀 필터와 `SecurityConfig`를 구축하여 인가 실패 및 만료 토큰을 안전하게 핸들링.

3. **사용자 암호화**:

- BCrypt 해시 함수를 통해 데이터베이스 내 비밀번호 단방향 암호화 저장.

---

## 🛠️ 3. 기술 스택 (Tech Stack)

| 구분              | 기술                | 상세 스펙              |
| :---------------- | :------------------ | :--------------------- |
| **Language**      | Java                | OpenJDK 17 (LTS)       |
| **Framework**     | Spring Boot         | 3.5.0                  |
| **Build Tool**    | Gradle              | Gradle Wrapper         |
| **Persistence**   | Spring Data JPA     | Hibernate ORM          |
| **Database**      | MariaDB             | MariaDB Java Client    |
| **Security**      | Spring Security     | 6.x & JJWT 0.12.3      |
| **External APIs** | Skyscanner RapidAPI | POST /api/v1/roundtrip |
|                   | Google Gemini AI    | 실시간 여행 코스 생성  |
| **Utility**       | Lombok              | 보일러플레이트 제거    |

---

## 📊 4. 핵심 REST API 명세 요약

| Method     | URI                                   | 설명                                                           |  인증  |
| :--------- | :------------------------------------ | :------------------------------------------------------------- | :----: |
| **POST**   | `/api/external/flights/search`        | 스카이스캐너 실시간 항공권 조회 (성인/소아/유아/좌석등급 매핑) | 비인증 |
| **POST**   | `/api/plans`                          | 신규 여행 일정 생성                                            |  필수  |
| **GET**    | `/api/plans/user/{userId}`            | 사용자의 전체 일정 목록 조회                                   |  필수  |
| **GET**    | `/api/plans/{planId}`                 | 특정 일정 상세 및 장소 목록 조회                               |  필수  |
| **DELETE** | `/api/plans/{planId}`                 | 여행 일정 단일 삭제                                            |  필수  |
| **PUT**    | `/api/plans/{planId}/places/sequence` | 드래그 앤 드롭 장소 순서 및 일차 변경 저장                     |  필수  |
| **POST**   | `/api/plans/{planId}/places/bulk`     | 배낭 장소들 일정에 일괄 벌크 추가                              |  필수  |
| **GET**    | `/api/backpack`                       | 사용자의 찜(북마크) 장소 목록 조회                             |  필수  |
| **POST**   | `/api/plans/ai-generate`              | Gemini AI 기반 맞춤형 코스 자동 생성                           |  필수  |
| **POST**   | `/api/plans/{planId}/copy`            | 추천/AI 일정 내 플래너로 복제                                  |  필수  |

---

## 🚀 5. 로컬 실행 가이드 (Getting Started)

### 요구사항 (Prerequisites)

- **Java**: JDK 17
- **Database**: MariaDB 10.6 이상

### 실행 순서

1. 레포지토리 클론:
   ```bash
   git clone https://github.com/junha2020/travel_app.git
   cd travel_app
   ```
2. 시크릿 설정 파일 생성 (src/main/resources/application-secret.properties):

   spring.datasource.url=jdbc:mariadb://localhost:3306/travel_app_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password

   rapidapi.skyscanner.key=YOUR_RAPIDAPI_KEY
   gemini.api.key=YOUR_GEMINI_KEY
   jwt.secret=YOUR_JWT_SECRET_KEY

3. Gradle 빌드 및 서버 구동:

   ./gradlew clean bootRun

• 서버는 기본 http://localhost:8080 포트에서 실행됩니다.
