FROM openjdk:8-jdk-alpine
COPY target/inventory-spring-boot-app.jar inventory-spring-boot-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "inventory-spring-boot-app.jar"]





