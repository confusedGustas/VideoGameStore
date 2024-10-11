FROM openjdk:21-jdk AS backend
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests
ENTRYPOINT ["java", "-jar", "target/video-game-store-0.0.1-SNAPSHOT.jar"]