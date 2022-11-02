FROM openjdk:8-jdk-alpine
EXPOSE 8080
COPY build/libs/*.jar ./application.jar
ENTRYPOINT ["java", "-jar", "./application.jar"]
