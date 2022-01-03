FROM openjdk:11-jre-slim-bullseye
RUN adduser --system --group springdocker
USER springdocker:springdocker
ARG JAR_FILE=app/build/libs/pets-database-simple.jar
COPY ${JAR_FILE} pets-database.jar
ENTRYPOINT ["java","-jar", \
"/pets-database.jar"]
# Environment variables to be prdvided in docker-compose
