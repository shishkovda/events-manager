FROM ubuntu:18.04
RUN apt update
RUN apt install -y default-jre
ARG JAR_FILE=target/events-manager-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]