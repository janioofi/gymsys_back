FROM openjdk:17
WORKDIR /app
COPY ./target/eurekaserver-1.0.jar app.jar
EXPOSE 8761
ENTRYPOINT java -jar app.jar