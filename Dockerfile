FROM openjdk:11

ARG DEPENDENCY=distribution
RUN echo ${DEPENDENCY}
COPY ${DEPENDENCY}/assignment-1.0.0-SNAPSHOT.jar /app/application.jar

ENTRYPOINT ["java", "-jar", "/app/application.jar"]

EXPOSE 8080
