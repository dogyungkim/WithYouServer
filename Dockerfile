# 최종 실행 이미지
FROM alpine:3.18.4

# Java 설치
RUN apk add --no-cache openjdk17

# 애플리케이션 디렉토리 생성
RUN mkdir /app

# 빌드된 JAR 파일 복사
COPY WithYou-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

# 애플리케이션 실행
CMD ["java", "-jar", "app.jar"]