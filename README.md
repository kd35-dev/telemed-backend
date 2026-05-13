# Telemedicine Backend

Spring Boot backend for a telemedicine platform. It analyzes symptoms with an external AI API, classifies severity, recommends specialists, manages doctors, allocates slots, books appointments, and exposes admin/audit APIs.

## Tech Stack

- Java 21
- Spring Boot 4
- Maven
- PostgreSQL
- JWT authentication
- Groq AI API
- Google Places API, optional

## Configuration

Secrets are not stored in `application.properties`. Copy `env.example` to `.env` for local development or set the same variables in your cloud provider.

Required:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `AI_API_KEY`

Optional:

- `JWT_EXPIRATION_MS`
- `GOOGLE_PLACES_API_KEY`
- `CORS_ALLOWED_ORIGINS`
- `APP_DEFAULT_ADMIN_USERNAME`
- `APP_DEFAULT_ADMIN_PASSWORD`
- `APP_DEFAULT_ADMIN_DISPLAY_NAME`
- `SPRING_PROFILES_ACTIVE=prod`

Use `APP_DEFAULT_ADMIN_USERNAME` and `APP_DEFAULT_ADMIN_PASSWORD` only to create the first admin account on an empty database. After the first login and password change, remove `APP_DEFAULT_ADMIN_PASSWORD` from production variables.

## Local Build

```bash
mvn -DskipTests package
```

The packaged jar is written to:

```text
target/app.jar
```

## Railway Deployment

Set the Railway service root to this backend folder. Railway can auto-detect the Maven/Spring Boot app from `pom.xml`; no Dockerfile is required.

Set production variables in Railway, especially:

```text
SPRING_PROFILES_ACTIVE=prod
CORS_ALLOWED_ORIGINS=https://your-vercel-frontend-url.vercel.app
```

If your production database is empty and you do not have migrations yet, create the schema first or temporarily use the dev profile for the first boot, then switch back to `prod`.
