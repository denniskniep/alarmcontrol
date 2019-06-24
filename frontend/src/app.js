import React from "react";
import { BrowserRouter as Router, Route } from "react-router-dom";
import { ApolloProvider } from "react-apollo";
import ApolloClient from "apollo-client";;
import AlertView from "./alertview/alertView";
import OrganisationsView from "./manage/organisationsView";
import Menu from "./menu";
import OrganisationView from "./manage/organisationView";


import { split } from 'apollo-link';
import { HttpLink } from 'apollo-link-http';
import { WebSocketLink } from 'apollo-link-ws';
import { getMainDefinition } from 'apollo-utilities';
import {InMemoryCache} from "apollo-cache-inmemory";
import Home from "./home";

// Create an http link:
const httpLink = new HttpLink({
  uri: "http://localhost:8080/graphql"
});

// Create a WebSocket link:
const wsLink = new WebSocketLink({
  uri: "ws://localhost:8080/subscriptions",
  options: {
    reconnect: true
  }
});

// using the ability to split links, you can send data to each link
// depending on what kind of operation is being sent
const link = split(
    // split based on operation type
    ({ query }) => {
      const { kind, operation } = getMainDefinition(query);
      return kind === 'OperationDefinition' && operation === 'subscription';
    },
    wsLink,
    httpLink
);

const client = new ApolloClient({
  link: link,
  cache: new InMemoryCache()
});

function App() {
  return (
    <ApolloProvider client={client}>
      <Router>
          <Menu/>
          <Route exact path="/app/" component={Home} />
          <Route exact path="/app/alertview/:id" component={AlertView} />
          <Route exact path="/app/manage/organisation" component={OrganisationsView} />
          <Route exact path="/app/manage/organisation/:id" component={OrganisationView} />
      </Router>
    </ApolloProvider>
  );
}

export default App;
