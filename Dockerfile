# syntax=docker/dockerfile:1

FROM openjdk:17-jdk-alpine as base

WORKDIR /app

COPY . .

RUN sh ./gradlew build

FROM openjdk:17-jdk-alpine as prod

WORKDIR /app

ARG APPNAME=chilpost-sb-0.0.1

COPY --from=base /app/build/libs/$APPNAME.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="\
  -server \
  -Xms256m \
  -Xmx512m \
  -XX:MetaspaceSize=256m \
  -XX:MaxMetaspaceSize=512m"

ENV PARAMS=""

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS app.jar $PARAMS"]
