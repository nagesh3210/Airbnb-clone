FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/service-registry-1.0.0.jar app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]