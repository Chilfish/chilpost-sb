FROM openjdk:17

WORKDIR /app

COPY build/libs/chilpost-sb-0.0.1.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
