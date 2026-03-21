FROM openjdk:27-ea-oraclelinux9
WORKDIR /app
COPY target/telemedicine-backend.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]