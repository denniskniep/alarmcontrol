FROM maven:3.6.1-jdk-11 AS build

WORKDIR /app

# Cache mvn dependencies as docker layer
COPY ./pom.xml /app
RUN mvn --batch-mode dependency:go-offline

COPY ./ /app
RUN mvn --batch-mode clean install

#--------------------------------------------------------
FROM openjdk:11-jre-slim

RUN set -eux; \
    apt-get update; \
    apt-get install -y gosu netcat;

RUN groupadd -g 1000 appuser && \
    useradd -r -u 1000 -g appuser appuser

COPY scripts/docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh

COPY --from=build /app/target/alarmcontrol-server.jar /app/alarmcontrol-server.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["/app/docker-entrypoint.sh"]
CMD ["-Xms1g", "-Xmx4g", "-Duser.timezone=UTC", "-jar", "/app/alarmcontrol-server.jar"]
