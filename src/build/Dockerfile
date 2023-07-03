# 빌드 단계에서 사용할 ARM 아키텍처에 맞는 JDK 이미지
FROM --platform=linux/arm64 gradle:jdk17 AS builder

# 소스 코드를 현재 디렉토리로 복사합니다.
COPY --chown=gradle:gradle . /app

# 작업 디렉터리 생성
WORKDIR /app

# Gradle Wrapper를 사용하여 프로젝트 빌드
RUN ./gradlew build --no-daemon

# 실행 단계에서 사용할 ARM 아키텍처에 맞는 JDK 이미지
FROM --platform=linux/arm64 openjdk:17.0-slim

EXPOSE 8080

WORKDIR /app
COPY --from=builder /app/build/libs/ /app/
ENTRYPOINT ["java", "-jar", "trendy-tracker.jar"]
