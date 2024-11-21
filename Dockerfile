# 베이스 이미지 설정
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=prod
# 빌드된 JAR 파일을 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/app.jar

# Docker 내부에서 복사된 파일 확인
RUN echo "Checking /app directory inside Docker:" && ls -al /app

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
