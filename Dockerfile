FROM openjdk:8

WORKDIR /usr/src/app
COPY ./classes/artifacts/inventory_project_jar inventory_project.jar


EXPOSE 8080
CMD ["java","-jar", "./inventory_project.jar"]




