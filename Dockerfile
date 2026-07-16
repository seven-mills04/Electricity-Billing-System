# ─── Build Stage ───────────────────────────────────────────────────
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
# Copy gradle wrapper & dependency files first for better layer caching
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true
# Copy source and build
COPY src ./src
RUN ./gradlew bootJar -x test --no-daemon

# ─── Run Stage ─────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
