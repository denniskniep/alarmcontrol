# Alarmcontrol

## Development
* Set SpringProfile to local i.e. via env variable `SPRING_PROFILES_ACTIVE=local`
* Set the env variable `GRAPHHOPPER_APIKEY=xxxxx-xxxx-xxxx-xxxx-xxxxxxxxx`
(register for a free account [here](https://graphhopper.com/dashboard/#/register) and generate your apikey) 

Start react client inside the folder `frontend` with `npm run start`

### Database
* **H2Console**: http://localhost:8080/h2-console

### GraphQL
* **Editor**: http://localhost:8080/graphiql
* **Schema**: http://localhost:8080/graphql/schema.json

#### Add an Organisation
```
mutation { 
  newOrganisation(
    name: "FF Meimbressen"
    addressLat: "51.406339"
    addressLng: "9.359186"
  ){id}
}
```

#### Add an Alert
```
mutation { 
  newAlert(
    keyword: "H1"
    organisationId: 1
    dateTime: "2019-05-03T12:23:32.456Z"
    description: ""
    address:"Hinter den Gärten 8, 34379 Calden"
  ){id}
}
```

#### Query an Alert
```
query {
  alertById(id: 1) {
    id
    keyword
    dateTime
    organisation {
      id
      name
    }
  }
}
```

### Client
The client is running by default at http://localhost:1234

## Components

### Server
* **Framework**: Spring Boot
* **DB-Setup**: Spring Data JPA with Hibernate. Bootstrapped by Liquibase
* **Data-API**: GraphQL
* **Routing**: https://docs.graphhopper.com/
* **Geocoding**: https://wiki.openstreetmap.org/wiki/Nominatim

### Client
* **Routing**: react-router
(https://reacttraining.com/react-router/web/example/basic)
* **GraphQl**: Apollo Client (https://www.apollographql.com/docs/react/essentials/get-started)
* **GUI**: React-Bootstrap (https://react-bootstrap.github.io/layout/grid/)


## ToDos
### Real-Time
Client should be up to date without manually refreshing the page. This is necessary if someone respond with a status.

> Polling is an excellent way to achieve near-realtime data without the complexity of setting up GraphQL subscriptions.

https://www.apollographql.com/docs/react/essentials/queries#refetching

### Docker
Dockerize it (Multistage)

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
### Geocoding house numbers
Some villages does not provide house number accurate information for geocoding via Openstreetmap Nominatim  
