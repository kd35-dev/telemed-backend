# Telemedicine Access for Healthcare (Backend)

A backend system for a telemedicine platform that analyzes user symptoms using an external AI service and recommends medical guidance and doctors based on severity.

This project is built using **Java and Spring Boot** to practice backend system design, REST API development, and scalable service architecture.

---

## Project Overview

The platform allows users to enter symptoms and receive AI-assisted analysis.  
Based on the severity of the condition, the system may suggest consulting a doctor and recommend specialists with available appointment slots.

The backend serves as the **core processing layer** that:
- receives user symptom inputs
- integrates with an external AI API
- processes medical logic
- recommends doctors and consultation times

---

## Key Features

- AI-powered symptom analysis
- Severity classification for symptoms
- Doctor recommendation based on specialization
- Appointment slot suggestions
- Structured REST API responses
- Input validation and error handling
- Modular backend architecture
- Scalable service-layer design

---

## Tech Stack

### Backend
- Java 17+
- Spring Boot

### API Development
- REST APIs
- Spring Web

### Database
- MongoDB

### Tools
- Maven
- Postman
- Git & GitHub

---

## Backend Architecture

The backend follows a **layered architecture** to maintain separation of concerns.


---

## Modules

### Module 1 – Backend Foundation
- Project setup
- Controller layer
- DTO structure
- Service layer
- Request validation
- API response wrapper

### Module 2 – AI Integration
- External AI API connection
- Symptom analysis

### Module 3 – Severity Logic
- Medical severity classification

### Module 4 – Doctor Management
- Doctor data storage
- Doctor APIs

### Module 5 – Doctor Recommendation
- Suggest specialists based on symptoms

### Module 6 – Symptom Logging
- Store symptom requests for analytics

### Module 7 - Admin Authentication
- Implemented JWT-based admin authentication and secured doctor management endpoints
---

### Development Workflow

- Design API contract

- Implement controller layer

- Implement service logic

- Integrate database

- Add external AI integration

- Test APIs using Postman

- Push code to GitHub

---

## Configuration (local & production)

Secrets and credentials are **not** stored in `application.properties`. Copy `env.example` to `.env` (or export variables in your shell) and set at least:

- `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `AI_API_KEY`

Optional: `GOOGLE_PLACES_API_KEY`, `CORS_ALLOWED_ORIGINS` (comma-separated; add your production frontend DNS).

- **Default profile:** `dev` (`spring.profiles.default=dev`) — JPA `ddl-auto=update` for local iteration.
- **Production:** set `SPRING_PROFILES_ACTIVE=prod` for stricter JPA settings and Actuator health probes.

GKE, Kubernetes Secrets, DNS, and ConfigMaps are documented in **`docs/DEPLOYMENT-GKE.md`**. The **Kubernetes / DevOps** layout (Kustomize, Ingress, HPA, PDB, Services) is under **`k8s/`** — start with **`k8s/README.md`**.

**Docker:** build/push to **Artifact Registry**, **`docker-compose`** local stack → **`docs/DOCKER.md`** (repo root `docker-compose.yml`).

---

### Future Improvements

- User authentication

- Patient medical history

- Appointment booking

- Admin dashboard

- Notification system

- Improved AI diagnosis accuracy
---
