Here’s a **README.md** draft for your **Fitness-WebApp** project. It covers all the technologies you listed and explains setup, architecture, and usage clearly.

---

# 🏋️‍♂️ Fitness-WebApp

A **Fitness Tracking Web Application** built with **Microservices Architecture** using **Spring Boot (Java 17)**.
The app integrates multiple databases (**Postgres, MongoDB, MySQL**) for different services, leverages **RabbitMQ** for event-driven communication, **Eureka** for service discovery, and **Docker** for containerization.

It also connects with **Gemini API** (Google AI) for generating personalized fitness insights.

---

## 🚀 Features

* **User Service (MySQL):** Manage users, authentication, and profiles.
* **Workout Service (Postgres):** Store and track workouts, progress, and schedules.
* **Nutrition Service (MongoDB):** Manage meal plans, nutrition logs, and calories.
* **Recommendation Service (Gemini API):** AI-powered personalized fitness tips.
* **Notification Service (RabbitMQ):** Asynchronous event-based notifications.
* **Service Discovery (Eureka):** Centralized registry for microservices.
* **API Gateway:** Unified entry point for client requests.
* **Containerization (Docker):** Deployable microservices with docker-compose.

---

## ⚙️ Tech Stack

* **Backend:** Java 17, Spring Boot
* **Databases:** MySQL, PostgreSQL, MongoDB
* **Messaging Queue:** RabbitMQ
* **Service Discovery:** Eureka
* **Containerization:** Docker, Docker Compose
* **AI Integration:** Gemini API (via API key)
* **Testing:** Postman

---

## 📦 Setup & Installation

### 1️⃣ Clone Repository

```bash
git clone https://github.com/yourusername/Fitness-WebApp.git
cd Fitness-WebApp
```

### 2️⃣ Configure Environment Variables

Create `.env` file in root:

```env
# Database
MYSQL_URL=jdbc:mysql://mysql:3306/users
MYSQL_USER=root
MYSQL_PASSWORD=root

POSTGRES_URL=jdbc:postgresql://postgres:5432/workouts
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

MONGO_URI=mongodb://mongo:27017/nutrition

# RabbitMQ
RABBITMQ_HOST=rabbitmq
RABBITMQ_PORT=5672
RABBITMQ_USER=guest
RABBITMQ_PASSWORD=guest

# Eureka
EUREKA_SERVER=http://eureka:8761/eureka

# Gemini API
GEMINI_API_KEY=your_gemini_api_key
```

### 3️⃣ Run with Docker

```bash
docker-compose up --build
```

This will start:

* Microservices (User, Workout, Nutrition, Recommendation, Notification)
* Databases (MySQL, Postgres, MongoDB)
* RabbitMQ
* Eureka Server
* API Gateway

### 4️⃣ Access Services

* **Eureka Dashboard:** `http://localhost:8761`
* **API Gateway:** `http://localhost:8080`
* **RabbitMQ Management UI:** `http://localhost:15672`
* **Postman Collections:** Available in `/postman/`

---

## 🧪 Testing with Postman

1. Import the collection from `/postman/Fitness-WebApp.postman_collection.json`.
2. Test endpoints for Users, Workouts, Nutrition, and Recommendations.
3. Verify inter-service communication (RabbitMQ & Eureka).

---

## 🔮 Future Improvements

* OAuth2 / JWT Authentication
* Kubernetes Deployment (Helm)
* GraphQL API Gateway
* CI/CD with GitHub Actions

---

## 🤝 Contributing

Contributions are welcome! Please fork the repo and submit a PR.

---

## 📜 License

MIT License.

---

Would you like me to also create a **docker-compose.yml** file for all microservices + databases + RabbitMQ + Eureka, so that you can run everything in one command?
