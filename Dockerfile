FROM openjdk:17-jdk-alpine
COPY target/petstore-0.0.1-SNAPSHOT.jar petstore-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=production", "-jar", "/petstore-0.0.1-SNAPSHOT.jar"]