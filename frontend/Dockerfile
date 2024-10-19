FROM node:lts-alpine AS frontend
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run format
CMD ["npm", "run", "dev", "--", "--config", "vite.config.docker.js"]
