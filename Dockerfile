FROM openjdk:8-jdk-alpine

WORKDIR /usr/src/app
COPY ./classes/artifacts/inventory_project_jar inventory_project.jar


EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=default", "./inventory_project.jar"]





