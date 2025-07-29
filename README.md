# WithYou

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/your-username/withyou-server)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)

**WithYou**는 여행의 추억을 공유하고 소통하는 소셜 미디어 플랫폼의 백엔드 API입니다. 이 프로젝트는 Spring Boot를 기반으로 구축되었으며, 사용자 인증, 게시물 관리, 클라우드 연동 등 다양한 기능을 제공합니다.

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
![Architecture](.docs/architecture.png)

### CI/CD 파이프라인
<img src=".docs/cicd_pipeline.png" width="750"/>


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