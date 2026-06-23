FROM eclipse-temurin:21-jre
WORKDIR /app

COPY target/iamryan-api-0.0.1-SNAPSHOT.jar /app/iamryan-api.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/iamryan-api.jar", "--server.port=8080"]
