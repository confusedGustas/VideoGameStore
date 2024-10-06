FROM openjdk:21-jdk AS backend
WORKDIR /app
COPY . .
ENTRYPOINT ["java", "-jar", "target/video-game-store-0.0.1-SNAPSHOT.jar"]