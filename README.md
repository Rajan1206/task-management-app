# 🗂️  Task Management App
A modular, secure, and scalable task management backend app built in Java using microservices architecture with Spring Boot, Kafka, PostgreSQL, and Docker. Supports user authentication, task CRUD operations and sending email notifications.

---

## 🧰 Tech Stack

- Java 17, Spring Boot
- Spring Security, JWT
Spring Kafka
- PostgreSQL
- Spring Cloud Gateway
- Docker & Docker Compose

## 🐳 How to Run Locally (Using Docker)

1. Clone this repository:
```bash
git clone https://github.com/Rajan1206/task-management-app.git
cd task-management-app
```

2. Iterate through each sub directory and build all micro service using below command :
```bash
# Go into each microservice, run a command (e.g., mvn clean install), then return back

cd auth-service
mvn clean install 
cd ..

cd task-service
mvn clean install 
cd ..

cd notification-service
mvn clean install 
cd ..

cd api-gateway
mvn clean install 
cd ..
```

3. Run all services with Docker Compose:
```bash
docker compose -f docker-compose.yml up --build
```

---

## 📫 API Endpoints

- Auth:
    - `POST /auth/signup`
    - `POST /auth/login`
    - `POST /auth/logout`
- Task:
    - `POST /create/task`
    - `GET /task`
    - `PUT /update/task/{id}`
    - `DELETE /task/{id}`
    - `GET /task/{id}`
    - `GET /tasks/{id}/complete`

> All `/tasks/**` endpoints are protected via JWT and routed through the API Gateway

---

## 🔐 Authentication

- Users receive a **JWT token** on login
- Include it in request header as:
  ```
  Authorization: Bearer <token>
  ```
- Gateway validates the token before routing to services

---

## 📬 Email Notifications

- On task creation/update/completion, `task-service` sends a Kafka event
- `notification-service` listens to this and sends an email via SMTP

---
