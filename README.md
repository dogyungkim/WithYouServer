# WithYou
<img width="2250" height="1104" alt="Image" src="https://github.com/user-attachments/assets/f22640ce-62be-4adb-84e4-8fb44990c930" />
여행의 추억을 공유하고 소통하는 소셜 미디어 플랫폼 WithYou의 백엔드 레포지토리입니다.

## 서비스 소개
<div style="display: flex; gap: 10px;">
  <img width="500" alt="Land1" src="https://github.com/user-attachments/assets/03c322ca-f06c-4745-8b3b-71eb10f1e3d4" />
  <img width="500" alt="Land2" src="https://github.com/user-attachments/assets/cc3169c9-0d50-48f2-9933-5e1d40da6ee9" />
  <img width="500" alt="Land4" src="https://github.com/user-attachments/assets/f3f3f159-2e37-49de-b923-cc171b246c61" />
  <img width="500" alt="Land3" src="https://github.com/user-attachments/assets/dd509db5-9cda-4920-8483-85708000301e" />
</div>


## ✨ 주요 기능

- **사용자 관리:** 회원가입, 로그인, 소셜 로그인 (OAuth 2.0)
- **인증/인가:** JWT (JSON Web Token) 기반의 안전한 인증 시스템
- **게시물 관리:** 여행기, 사진 등 게시물 CRUD
- **소셜 기능:** 댓글, 좋아요, 팔로우
- **클라우드 연동:** AWS S3를 이용한 미디어 파일 업로드 및 관리
- **API 문서:** Swagger (Springdoc OpenAPI)를 통한 API 명세 자동화

## 🛠️ 기술 스택

### Backend
<p>
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" />
  <img src="https://img.shields.io/badge/H2-596C7A?style=for-the-badge&logo=h2&logoColor=white" />
</p>

### Authentication
<p>
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white" />
  <img src="https://img.shields.io/badge/OAuth_2.0-2396F3?style=for-the-badge&logo=oauth&logoColor=white" />
</p>

### CI/CD
<p>
  <img src="https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
</p>

### Build & Dependency
<p>
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" />
</p>

### Test
<p>
  <img src="https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=junit5&logoColor=white" />
  <img src="https://img.shields.io/badge/Mockito-6A49A8?style=for-the-badge&logo=mockito&logoColor=white" />
</p>


## 🏛️ 아키텍처

### 서비스 아키텍처
<img width="1101" height="620" alt="서비스 아키텍처" src="https://github.com/user-attachments/assets/b1a99e9d-7fd7-4b00-9e0d-3deb4cc51be3" />

## 📁 디렉토리 구조
```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── UMC
│   │   │       └── WithYou
│   │   │           ├── WithYouApplication.java
│   │   │           ├── common      # 공통 모듈 (API 응답, 예외 처리, 설정)
│   │   │           ├── feature     # 도메인별 비즈니스 로직
│   │   │           │   ├── auth
│   │   │           │   ├── member
│   │   │           │   ├── post
│   │   │           │   └── ...
│   │   │           ├── infra       # 외부 시스템 연동 (AWS S3)
│   │   │           └── support     # 인증 필터, Argument Resolver
│   │   └── resources
│   │       └── application.yml
│   └── test
│       └── java
└── build.gradle
```
