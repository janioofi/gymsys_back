FROM openjdk:17
WORKDIR /app
COPY ./target/ms-email-1.0.jar app.jar

ARG URL
ARG USER
ARG PASSWORD
ARG RABBITMQ
ARG EMAIL
ARG PASSWORD_EMAIL
ARG QUEUE
ARG EUREKA

ENTRYPOINT java -jar app.jar