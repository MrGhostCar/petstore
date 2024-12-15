# Petsrore backend
## How to deploy locally:
./mvnw clean package -DskipTests
docker build --no-cache -t home/petstore-be .
docker-compose up
