FROM node:10.16.0-alpine AS frontend-build

WORKDIR /app

# prevents call to lscpu and stop freezing build
ENV PARCEL_WORKERS=4

# Cache npm dependencies as docker layer
COPY ./frontend/package.json /app/
COPY ./frontend/package-lock.json /app/
RUN npm ci

COPY ./frontend /app
RUN npm run build

#--------------------------------------------------------
FROM maven:3.6.1-jdk-11 AS backend-build

WORKDIR /app

# Cache mvn dependencies as docker layer
COPY ./pom.xml /app
RUN mvn --batch-mode dependency:go-offline

COPY ./src /app/src
COPY --from=frontend-build /app/dist /app/src/main/resources/static

RUN mvn --batch-mode clean install

# copy all testfiles into a folder
RUN mkdir -p /build/artifacts/testresults && \
    find /app/ -name "TEST-*.xml" -exec cp {} /build/artifacts/testresults \;

#--------------------------------------------------------
FROM openjdk:11-jre-slim

RUN set -eux; \
    apt-get update; \
    apt-get install -y gosu netcat;

RUN groupadd -g 1000 appuser && \
    useradd -r -u 1000 -g appuser appuser

COPY scripts/docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh

COPY --from=backend-build /app/target/alarmcontrol-server.jar /app/alarmcontrol-server.jar

# Copy buildresults into single folder
COPY --from=backend-build /build/artifacts/testresults /build/artifacts/testresults
COPY --from=backend-build /app/target/alarmcontrol-server.jar /build/artifacts/alarmcontrol-server.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["/app/docker-entrypoint.sh"]
CMD ["-Xms1g", "-Xmx4g", "-Duser.timezone=UTC", "-jar", "/app/alarmcontrol-server.jar"]
