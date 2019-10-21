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
import Home from "./home/home";
import {ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import AlertViewSwitcher from "./alertview/alertViewSwitcher";
import configuration from "./config/configuration";
import CurrentOrganisationContainer from "./currentOrganisationContainer";
import AlertsByOrganisationView from "./manage/alertsByOrganisationView";
import NotificationsByOrganisationView from "./manage/notifications/notificationsByOrganisationView";
import AaosByOrganisationView from "./manage/aaos/aaosByOrganisationView";
import AaoView from "./manage/aaoView";

// Create an http link:
const httpLink = new HttpLink({
  uri: "http://" + configuration.getServer() + "/graphql"
});

// Create a WebSocket link:
const wsLink = new WebSocketLink({
  uri: "ws://" + configuration.getServer() + "/subscriptions",
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
        <CurrentOrganisationContainer>
          <Menu/>
          <ToastContainer
              position="top-right"
              autoClose={3000}
              hideProgressBar={false}
              newestOnTop={false}
              closeOnClick
              rtl={false}
              pauseOnVisibilityChange
              draggable
              pauseOnHover
          />
          <AlertViewSwitcher/>
          <Route exact path="/app/" component={Home} />
          <Route exact path="/app/alertview/:id" component={AlertView} />
          <Route exact path="/app/manage/organisation" component={OrganisationsView} />
          <Route exact path="/app/manage/organisation/:id" component={OrganisationView} />
          <Route exact path="/app/manage/organisation/:id/alerts" component={AlertsByOrganisationView} />
          <Route exact path="/app/manage/organisation/:id/notifications" component={NotificationsByOrganisationView} />
          <Route exact path="/app/manage/organisation/:id/aaos" component={AaosByOrganisationView} />
          <Route exact path="/app/manage/alerts" component={AlertsByOrganisationView} />
        </CurrentOrganisationContainer>
      </Router>
    </ApolloProvider>
  );
}

export default App;
