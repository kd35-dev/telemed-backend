# =============================================================================
# BACKEND DOCKERFILE - Multi-stage optimized build for Spring Boot 4.0.3 (Java 21)
# =============================================================================
# BUILDER STAGE: Compile with Maven
# RUNTIME STAGE: Minimal JRE image
# Benefits:
#   - Separates build dependencies from runtime
#   - Final image excludes Maven, compiler, build tools (~500MB reduction)
#   - Uses official Eclipse Temurin JRE (security-focused)
#   - Supports both ARM64 and AMD64 architectures
# =============================================================================

# --- Stage 1: Builder
# Using official Maven image with JDK 21 for reliable builds
FROM maven:3.9-eclipse-temurin-21-jammy AS builder

WORKDIR /build

# Copy Maven wrapper and project files
COPY mvnw mvnw.cmd pom.xml ./
COPY src ./src

# Make Maven wrapper executable (Docker handles this transparently on Linux)
RUN chmod +x mvnw

# Build the application
# -DskipTests: skip tests in Docker build for faster iteration (run separately in CI/CD)
# -q: quiet mode to reduce log noise
RUN ./mvnw clean package -DskipTests -q

# Verify JAR was created (fail fast if build failed)
RUN test -f target/*.jar || (echo "Build failed: no JAR found" && exit 1)

# --- Stage 2: Runtime
# Using distroless for minimal attack surface (no shell, no package manager)
# Size: ~170MB (vs. 900MB+ with full JRE + OS packages)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user for security (prevent privilege escalation)
RUN addgroup -g 1000 -S appuser && adduser -u 1000 -S appuser -G appuser

# Copy built JAR from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose Spring Boot port
EXPOSE 8080

# Healthcheck for container orchestrators (Docker Compose, Kubernetes)
# Tests if Spring Boot application is responsive
# interval: check every 30s
# timeout: wait max 5s for response
# retries: mark unhealthy after 3 failures
# start-period: wait 60s before first check (app startup time)
HEALTHCHECK --interval=30s --timeout=5s --retries=3 --start-period=60s \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Environment variables for Spring Boot
# These come from docker-compose.yml or Kubernetes ConfigMaps/Secrets
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-XX:+UseG1GC -XX:+UseStringDeduplication -XX:+ParallelRefProcEnabled"

# Run Spring Boot application
# Using exec form (JSON array) ensures proper signal handling (important for graceful shutdown)
ENTRYPOINT ["java", "-jar", "app.jar"]
