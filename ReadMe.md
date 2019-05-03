# Alarmcontrol

## Development
Set SpringProfile to local i.e. via env variable `SPRING_PROFILES_ACTIVE=local`

Start react client inside the folder `frontend` with `npm run start`

### Database
http://localhost:8080/h2-console

### GraphQL
Editor:
http://localhost:8080/graphiql

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

## Websockets Example
https://github.com/RatneshChauhan/springboot-react-chatroom/tree/master/Client