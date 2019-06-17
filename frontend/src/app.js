import React from "react";
import { BrowserRouter as Router, Route } from "react-router-dom";
import { ApolloProvider } from "react-apollo";
import ApolloClient from "apollo-boost";
import AlertView from "./alertview/alertView";
import OrganisationsView from "./manage/organisationsView";
import Menu from "./menu";
import OrganisationView from "./manage/organisationView";

const client = new ApolloClient({
  uri: "http://localhost:8080/graphql"
});

function App() {
  return (
    <ApolloProvider client={client}>
      <Router>
          <Menu/>
          <Route exact path="/" component={Home} />
          <Route exact path="/alertview/:id" component={AlertView} />
          <Route exact path="/manage/organisation" component={OrganisationsView} />
          <Route exact path="/manage/organisation/:id" component={OrganisationView} />
      </Router>
    </ApolloProvider>
  );
}

function Home() {
  return (
      <div>
        <h2>Home</h2>
      </div>
  );
}

export default App;
