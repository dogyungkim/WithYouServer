# Docker 이미지 빌드 히스토리

## WithYou 프로젝트 Docker 이미지 빌드 기록

| 날짜 | 시간 | 태그 | 명령어 | 플랫폼 | 설명 |
|---|---|---|---|---|---|
| 2025-08-15 | 19:23:00 | `prunsoli/withyou-test:0.0.4` | `docker buildx build --platform linux/amd64 -t prunsoli/withyou-test:0.0.4 --push .` | linux/amd64 | fix: JWT 슈퍼토큰 비교 로직 및 Swagger 경로 수정 |
| 2025-08-14 | 23:02:21 | `prunsoli/withyou-test:0.0.3` | `docker buildx build --platform linux/amd64 -t prunsoli/withyou-test:0.0.3 --push .` | linux/amd64 | feat: 테스트용 JWT 등록 및 JWT TokenProvider 수정 |
| 2025-07-10 | 15:36:09 | `prunsoli/withyou-test:notice-as` | `docker buildx build --platform linux/amd64 -t prunsoli/withyou-test:notice-as --push .` | linux/amd64 | notice 성능 개선 이전 버전 |
| 2025-07-09 | 17:05:01 | `prunsoli/withyou-test:notice-to` | `docker buildx build --platform linux/amd64 -t prunsoli/withyou-test:notice-to --push .` | linux/amd64 | notice 성능 개선 버전 |
| 2025-07-09 | 16:59:27 | `prunsoli/withyou-test:notice-be` | `docker buildx build --push -t prunsoli/withyou-test:notice-be .` | - | notice 백엔드 개발 버전 |
| 2025-04-17 | 14:44:37 | `prunsoli/withyou-test:0.0.2` | `docker buildx build --platform linux/amd64 -t prunsoli/withyou-test:0.0.2 --push .` | linux/amd64 | 버전 0.0.2 |

## Docker 설정 정보

### Dockerfile 구성
- **베이스 이미지**: gradle:7.6-jdk17 (빌드 단계), eclipse-temurin:17-jre-alpine (실행 단계)
- **Java 버전**: 17
- **빌드 도구**: Gradle
- **포트**: 8080
- **JAR 파일**: WithYou-0.0.1-SNAPSHOT.jar

### docker-compose.yml 정보
- **서비스명**: withyou-app (현재 주석 처리됨)
- **이미지**: prunsoli/withyou-test:0.0.1
- **포트 매핑**: 8080:8080
- **의존성**: Redis
- **네트워크**: withyou-network

### 빌드 패턴
- 빌드 도구: `docker buildx`
- 플랫폼: `linux/amd64`
- 저장소: `prunsoli/withyou-test`
- 자동 푸시: 활성화

## 참고사항
- Docker Hub: `prunsoli/withyou-test`
- 현재 브랜치: `refactor/notice`
- 테스트 제외: `-x test`