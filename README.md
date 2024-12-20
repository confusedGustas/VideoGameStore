# Video Game Store Application

A Video Game Store application built using PostgreSQL, Liquibase, Spring Boot, Spring Sessions, and Vue.js.

## Features

- Login and Registration
- Game listing, editing, and deletion
- Spring Session-based authentication
- Filtering and sorting
- Simulated checkout process
- Change password, username, and email

## Prerequisites

Ensure the following tools are installed before setting up the application:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/)
- [Apache Maven](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)
- (Optional) [Docker Desktop](https://www.docker.com/products/docker-desktop/)

## Setup

1. **Clone the repository:**

    ```bash
    git clone https://github.com/confusedGustas/VideoGameStore
    cd VideoGameStore
    ```

2. **Set up environment variables:**

   create a .env file in the root directory and add the following:

    ```properties
   DB_URL=jdbc:postgresql://localhost:5432/video_game_store
   DOCKER_DB_URL=jdbc:postgresql://database:5432/video_game_store
   DB_NAME=video_game_store
   DB_USER=postgres
   DB_PASSWORD=admin
   SPRING_USER=admin
   SPRING_PASSWORD=admin

3. **Configure PostgreSQL:**

    - Create a new database named `video_game_store`.
    - Leave the default settings as they are. Spring Boot will create the necessary tables.
    - To adjust settings, edit the `.env` file.

4. **Build and run the application:**

    ```bash
    cd frontend
    npm install
    npm run dev
    cd ..
    ./mvnw spring-boot:run
    ```

    - The Spring Boot backend will start at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).
    - The Vue.js frontend will run at [http://localhost:8081](http://localhost:8081).

   *(Optional)* To run tests, execute:

    ```bash
    ./mvnw test
    ```

   Ensure Docker Desktop is running, as the tests utilize Docker containers.

## Running with Docker

To run the application using Docker, follow these steps:

1. Ensure Docker Desktop is installed and running.
2. Build and run the Docker containers:

    ```bash
    docker compose up
    ```

   This command will:
    - Start the PostgreSQL database container.
    - Launch the Spring Boot backend.
    - Launch the Vue.js frontend.

3. Access the application:
    - Backend: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    - Frontend: [http://localhost:8081](http://localhost:8081)

   Use `docker compose down` to stop the containers when done.

## License

This project is licensed under the [MIT License](./LICENSE).
