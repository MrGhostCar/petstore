# Petstore backend

## Development:
You have to set environment variable IntelliJ : spring.profiles.active=development
<br>
And the environment variables listed in docker-compose.yaml

## Build & Deploy:
Copy built front-end folder into src/main/resources/static
<br>
then:
```
./mvnw clean package -DskipTests
docker build --no-cache -t home/petstore-be .
docker-compose up
```