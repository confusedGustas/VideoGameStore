FROM openjdk:21-jdk AS build
WORKDIR /app
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./
RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B
COPY src ./src
RUN ./mvnw clean package -DskipTests
FROM openjdk:21-jdk AS runtime
WORKDIR /app
COPY --from=build /app/target/VideoGameStore-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]