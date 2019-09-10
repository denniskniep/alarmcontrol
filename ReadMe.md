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

Execute docker-compose
```
sudo \
DATABASE_NAME=alarmcontrol \
DATABASE_USER=alarmcontrol \
DATABASE_PASSWORD=xxx \
GRAPHHOPPER_APIKEY=xxx \
MAPBOX_ACCESS_TOKEN=xxx \
docker-compose up
```


### From SourceCode
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
GRAPHHOPPER_APIKEY=xxx \
MAPBOX_ACCESS_TOKEN=xxx \
docker-compose up --build
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

## Start with Environment

Download docker-compose files
```
curl https://raw.githubusercontent.com/denniskniep/alarmcontrol/master/docker-compose.yaml -o docker-compose.yaml
```

```
curl https://raw.githubusercontent.com/denniskniep/alarmcontrol/master/docker-compose.logging.yaml -o docker-compose.logging.yaml
```

Execute docker-compose
```
sudo \
DATABASE_NAME=alarmcontrol \
DATABASE_USER=alarmcontrol \
DATABASE_PASSWORD=xxx \
GRAPHHOPPER_APIKEY=xxx \
MAPBOX_ACCESS_TOKEN=xxx \
docker-compose \
-f docker-compose.yaml \
-f docker-compose.logging.yaml \
up
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
