# ---------- build stage ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# copy maven metadata first for better caching
COPY pom.xml .
RUN mvn -B -f pom.xml dependency:go-offline

# copy source and build
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- runtime stage ----------
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# copy jar from build stage (cập nhật tên jar nếu thay đổi artifactId/version)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# optional: add a non-root user for security (uncomment if preferred)
# RUN addgroup -S app && adduser -S -G app app
# USER app

ENTRYPOINT ["java","-jar","/app/app.jar"]
