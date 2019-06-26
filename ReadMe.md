# Alarmcontrol

## Getting Started
Use docker-compose for testing (not for production)

```
sudo \
DATABASE_NAME=alarmcontrol \
DATABASE_USER=alarmcontrol \
DATABASE_PASSWORD=xxx \
GRAPHHOPPER_APIKEY=xxx \
MAPBOX_ACCESS_TOKEN=xxx \
docker-compose up --build
```

## Development
See [docs/development.md](docs/development.md)

## ToDos

### Security
Activate Spring Security (CSRF + AuthN, AuthZ)

Keep it simple at the beginning: 
ADMIN can read and write
USER can only read

--> Specify ADMIN User (name,password) via ENV Variables (if Password empty generate a password? and print to log)

Later there could be dedicated permissions for each organisation

### Routing/Geocoding API Error Handling
What should be done if the Address can not be geocoded

## Known Problems
### Geocoding house numbers with Nominatim
Some villages does not provide house number accurate information for geocoding via Openstreetmap Nominatim 

**Workaround:**
Use MapBox for geocoding


### Apollo Subscription generates React warning
`Can't perform a React state update on an unmounted component.`

There is already an issue created in Apollo Repository
https://github.com/apollographql/react-apollo/issues/2681
