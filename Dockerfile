# 최종 실행 이미지
FROM alpine:3.18.4

# Java 설치
RUN apk add --no-cache openjdk17

# 애플리케이션 파일을 컨테이너에 복사
COPY build/libs/WithYou-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 실행
CMD ["java", "-jar", "/app.jar"]