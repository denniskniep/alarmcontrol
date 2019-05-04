# Alarmcontrol

## Development
Set SpringProfile to local i.e. via env variable `SPRING_PROFILES_ACTIVE=local`

Start react client inside the folder `frontend` with `npm run start`

### Database
* **H2Console**: http://localhost:8080/h2-console

### GraphQL
* **Editor**: http://localhost:8080/graphiql
* **Schema**: http://localhost:8080/graphql/schema.json

#### Add an Alert
```
mutation { 
  newAlert(
    keyword: "H1Y"
    organisationId: 1
    raw: "..."
    active: true
    dateTime: "2019-05-03T12:23:32.456Z"
    description: ""
    addressType: "street"
    address:"Hauptstraße 1, 12345 Berlin"
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
  }
}
```

### Client
http://localhost:1234

## Architecture
### Client
* **Routing**: react-router
(https://reacttraining.com/react-router/web/example/basic)
* **GraphQl**: Apollo Client (https://www.apollographql.com/docs/react/essentials/get-started)


## ToDos

### Real-Time
Client should be up to date without manually refreshing the page. This is necessary if someone respond with a status.

> Polling is an excellent way to achieve near-realtime data without the complexity of setting up GraphQL subscriptions.

https://www.apollographql.com/docs/react/essentials/queries#refetching

