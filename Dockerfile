# Backend
FROM openjdk:21-jdk AS backend
COPY ./target/video-game-store-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# Frontend
#FROM node:lts-alpine as frontend
#WORKDIR /app
#COPY frontend/package*.json ./
#RUN npm install
#COPY frontend ./
#RUN npm run build
#CMD ["npm", "start"]

