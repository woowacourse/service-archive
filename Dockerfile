FROM adoptopenjdk:8-jdk-hotspot AS builder

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src

RUN chmod +x ./gradlew

RUN ./gradlew bootJar

FROM adoptopenjdk:8-jdk-hotspot
COPY --from=builder build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-server", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.config.location=classpath:/config/", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]

