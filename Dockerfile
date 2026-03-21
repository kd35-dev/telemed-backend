FROM openjdk:27-ea-oraclelinux9
WORKDIR /app
COPY target/telemedicine-backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]