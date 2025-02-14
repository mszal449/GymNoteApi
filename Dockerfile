FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=docker
CMD ["sh", "-c", "java -jar target/*.jar"]