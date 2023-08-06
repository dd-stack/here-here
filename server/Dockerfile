FROM openjdk:17-jdk-alpine

# 작업 디렉토리 지정
WORKDIR /app

# 빌드한 JAR 파일을 Docker 컨테이너에 복사
COPY build/libs/Here-0.0.1-SNAPSHOT-plain.jar /app/here.jar

# 애플리케이션 실행에 필요한 포트
EXPOSE 8080

# Docker 컨테이너가 실행될 때 수행될 명령어
ENTRYPOINT ["java", "-jar", "/app/here.jar"]