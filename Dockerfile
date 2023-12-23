# syntax=docker/dockerfile:1

FROM openjdk:19-jdk-alpine

WORKDIR /app

ARG APPNAME=chilpost-sb-0.0.1

# We should build the jar file first
COPY ./build/libs/$APPNAME.jar /app/app.jar
COPY files/avatars/* /app/files/avatars

ENV JAVA_OPTS="\
  -server \
  -Xms256m \
  -Xmx512m \
  -XX:MetaspaceSize=256m \
  -XX:MaxMetaspaceSize=512m"

ENV PARAMS=""

EXPOSE 8080

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS app.jar $PARAMS"]
