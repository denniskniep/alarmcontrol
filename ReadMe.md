# Alarmcontrol
[![AzureDevOps build](https://dev.azure.com/kniepdennis/Alarmcontrol/_apis/build/status/Alarmcontrol-CI?branchName=master)](https://dev.azure.com/kniepdennis/Alarmcontrol/_build?definitionId=3)
[![GitHub release](https://img.shields.io/github/release/denniskniep/alarmcontrol.svg)](https://github.com/denniskniep/alarmcontrol/releases)

## Quick Start

### From DockerHub
On Release the image is pushed to this [DockerHub Repository](https://hub.docker.com/r/denniskniep/alarmcontrol)

Download docker-compose file
```
curl https://raw.githubusercontent.com/denniskniep/alarmcontrol/master/docker-compose.yaml -o docker-compose.yaml
```

Configure EnvironmentVariables
The `GRAPHHOPPER_APIKEY` and the `MAPBOX_ACCESS_TOKEN` environment variables are optional. They are used for navigation and geocoding purposes.
Set the `GRAPHHOPPER_APIKEY`: Register for a free account [here](https://graphhopper.com/dashboard/#/register) and generate your apikey. 
Set the `MAPBOX_ACCESS_TOKEN`: register for a free account [here](https://www.mapbox.com/) and generate your accesstoken


The `FIREBASE_*` and `PAGER_URL` environment variables are optional. They are used for push notifications
Register for a free account [here](https://firebase.google.com/) and setup the firebase project with [this guide](firebase_setup.md)


Execute docker-compose
```
sudo \
DATABASE_NAME=alarmcontrol \
DATABASE_USER=alarmcontrol \
DATABASE_PASSWORD=xxx \
GRAPHHOPPER_APIKEY=xxx \
MAPBOX_ACCESS_TOKEN=xxx \
FIREBASE_PUSH_AUTHORIZATION_HEADER=xxx \
FIREBASE_USERNAME=xxx \
FIREBASE_PASSWORD=xxx \
FIREBASE_PROJECT_ID=xxx \
FIREBASE_PUBLIC_API_KEY=xxx \
FIREBASE_APP_ID=xxx \
FIREBASE_MESSAGING_SENDER_ID=xxx \
PAGER_URL=xxx \
docker-compose up
```

## Start with Environment

Download docker-compose files
```
curl https://raw.githubusercontent.com/denniskniep/alarmcontrol/master/docker-compose.yaml -o docker-compose.yaml
```

```
curl https://raw.githubusercontent.com/denniskniep/alarmcontrol/master/docker-compose.logging.yaml -o docker-compose.logging.yaml
```

```
curl https://raw.githubusercontent.com/denniskniep/alarmcontrol/master/docker-compose.notifications.yaml -o docker-compose.notifications.yaml
```

Execute docker-compose
```
sudo \
DATABASE_NAME=alarmcontrol \
DATABASE_USER=alarmcontrol \
DATABASE_PASSWORD=xxx \
GRAPHHOPPER_APIKEY=xxx \
MAPBOX_ACCESS_TOKEN=xxx \
FIREBASE_PUSH_AUTHORIZATION_HEADER=xxx \
FIREBASE_USERNAME=xxx \
FIREBASE_PASSWORD=xxx \
FIREBASE_PROJECT_ID=xxx \
FIREBASE_PUBLIC_API_KEY=xxx \
FIREBASE_APP_ID=xxx \
FIREBASE_MESSAGING_SENDER_ID=xxx \
PAGER_URL=xxx \
docker-compose \
-f docker-compose.yaml \
-f docker-compose.logging.yaml \
-f docker-compose.notifications.yaml \
up
```

### Developbuild from SourceCode with Environment
Clone repository with GIT
```
git clone https://github.com/denniskniep/alarmcontrol.git
```

Execute docker-compose
```
sudo \
DATABASE_NAME=alarmcontrol \
DATABASE_USER=alarmcontrol \
DATABASE_PASSWORD=xxx \
KEYCLOAK_FRONTEND_URL=http://localhost:8090/auth \
KEYCLOAK_ADMIN_USER=admin \
KEYCLOAK_ADMIN_PASSWORD=xxx \
KEYCLOAK_DATABASE_USER=keycloak \
KEYCLOAK_DATABASE_PASSWORD=xxx \
GRAPHHOPPER_APIKEY=xxx \
MAPBOX_ACCESS_TOKEN=xxx \
FIREBASE_PUSH_AUTHORIZATION_HEADER=xxx \
FIREBASE_USERNAME=xxx \
FIREBASE_PASSWORD=xxx \
FIREBASE_PROJECT_ID=xxx \
FIREBASE_PUBLIC_API_KEY=xxx \
FIREBASE_APP_ID=xxx \
FIREBASE_MESSAGING_SENDER_ID=xxx \
PAGER_URL=xxx \
docker-compose \
-f docker-compose-dev.yaml \
-f docker-compose.logging.yaml \
-f docker-compose.notifications.yaml \
up --build
```

### From Release Jar
On Release the fat JAR is uploaded to the [Release Page](https://github.com/denniskniep/alarmcontrol/releases/latest)

Download the latest JAR
```
curl -L https://github.com/denniskniep/alarmcontrol/releases/download/vX.X.X-XX/alarmcontrol-server.jar -o alarmcontrol-server.jar
```

Execute it 
```
java -jar alarmcontrol-server.jar 
```

## Development
See [docs/development.md](docs/development.md)

## Creating a Release
See [docs/release.md](docs/release.md)

## Changelog
See [CHANGELOG.md](CHANGELOG.md)

## Known Problems
### Geocoding house numbers with Nominatim
Some villages does not provide house number accurate information for geocoding via Openstreetmap Nominatim 

**Workaround:**
Use MapBox for geocoding


### Apollo Subscription generates React warning
`Can't perform a React state update on an unmounted component.`

There is already an issue created in Apollo Repository
https://github.com/apollographql/react-apollo/issues/2681


### Graylog without content pack autoload
You have to apply the content pack manually 
see https://github.com/Graylog2/graylog2-server/pull/6096
