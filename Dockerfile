FROM openjdk:21-jdk AS backend
COPY ./target/video-game-store-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]