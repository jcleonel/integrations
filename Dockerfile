FROM openjdk:21-jdk
COPY target/*.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]