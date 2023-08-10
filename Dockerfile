# 빌드 단계에서 사용할 ARM 아키텍처에 맞는 JDK 이미지
FROM gradle:jdk17 AS builder

# 소스 코드를 현재 디렉토리로 복사합니다.
COPY --chown=gradle:gradle . /app

# 작업 디렉터리 생성
WORKDIR /app

# Gradle Wrapper를 사용하여 프로젝트 빌드
RUN chmod +x ./gradlew
RUN ./gradlew clean build --no-daemon -x test

# 실행 단계에서 사용할 ARM 아키텍처에 맞는 JDK 이미지
FROM openjdk:17.0-slim

# 필요한 라이브러리 설치
RUN apt-get update && apt-get install -y libglib2.0-0 libnss3 libxcb1 wget unzip

# Chromium 관련 패키지 설치
RUN apt-get install -y chromium-common=112.0.5615.138-1~deb11u1 chromium-sandbox
RUN apt-get install -y chromium=112.0.5615.138-1~deb11u1 chromium-driver=112.0.5615.138-1~deb11u1

# Chromedriver 복사
RUN cp /usr/bin/chromedriver /usr/local/bin/

EXPOSE 8080

WORKDIR /app
COPY --from=builder /app/build/libs/ /app/
ENTRYPOINT ["java", "-jar", "trendy-tracker.jar"]
