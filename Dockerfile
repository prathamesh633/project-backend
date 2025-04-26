FROM openjdk:17-jdk-slim
COPY target/*.jar /opt/
EXPOSE 8080
CMD java -jar /opt/spring-backend-v1.jar