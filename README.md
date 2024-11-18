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
    git clone https://github.com/confusedGustas/Video-Game-Store
    cd video-game-store
    ```

2. **Set up environment variables:**

   Run the following command in the root directory of the project (either in your terminal or IDE console) to create a `.env` file. You can customize the variables or use the defaults. Ensure the credentials match your configured PostgreSQL credentials:

    ```bash
    (echo DB_URL=jdbc:postgresql://localhost:5432/video_game_store&& echo DOCKER_DB_URL=jdbc:postgresql://database:5432/video_game_store&& echo.&& echo DB_NAME=video_game_store&& echo DB_USER=postgres&& echo DB_PASSWORD=admin&& echo.&& echo SPRING_USER=admin&& echo SPRING_PASSWORD=admin) > .env
    ```

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

    - The Spring Boot backend will start at [http://localhost:8080](http://localhost:8080).
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
    - Backend: [http://localhost:8080](http://localhost:8080)
    - Frontend: [http://localhost:8081](http://localhost:8081)

   Use `docker compose down` to stop the containers when done.

## License

This project is licensed under the [MIT License](./LICENSE).
