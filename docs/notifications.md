## Add new Contact

### Backend
* Add a new package in `com/alarmcontrol/server/notifications/messaging`
* Add a class that implements Contact 
* Add a class that extends AbstractMessageService (this must be a @Service)
* Add a class that do the CRUD Operations for the new Contact via GraphQL (extends GraphQLMutationResolver)
* Add the CRUD Operations to the GraphQL Schema 
* Add new Type to the GraphQL Schema 
* Add to the JsonSubTypes in the Contact interface
* Add to the Gql SchemaParserDictionary in the InterfaceConfiguration class

### Frontend

