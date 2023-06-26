FROM gradle:jdk17 AS builder

# 소스 코드를 현재 디렉토리로 복사합니다.
COPY --chown=gradle:gradle ../../. /app

# 작업 디렉터리 생성
WORKDIR /app

# Gradle Wrapper를 사용하여 프로젝트 빌드
RUN gradle build --no-daemon

FROM openjdk:17.0-slim
EXPOSE 8080
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/trendy-tracker.jar
ENTRYPOINT ["java", "-jar", "trendy-tracker.jar"]