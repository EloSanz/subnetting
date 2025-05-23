FROM openjdk:17-slim as build
WORKDIR /workspace/app

# Install curl for downloading Maven wrapper
RUN apt-get update && apt-get install -y curl

# Copy Maven wrapper and POM
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source
COPY src ./src

# Build application
RUN ./mvnw package -DskipTests

FROM openjdk:17-slim
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"] 