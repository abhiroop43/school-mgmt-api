# Start with a base image containing Java runtime
FROM eclipse-temurin:21-jdk-alpine

# Add Maintainer Info
LABEL maintainer="abhiroop43@example.com"

# Make port 8919 available to the world outside this container
EXPOSE 8919

# The application's jar file
COPY target/schoolmgmtapi-0.0.1-SNAPSHOT.jar app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]

# add postgres dependency