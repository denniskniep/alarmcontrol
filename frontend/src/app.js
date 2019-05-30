import React from "react";
import { BrowserRouter as Router, Route } from "react-router-dom";
import { ApolloProvider } from "react-apollo";
import ApolloClient from "apollo-boost";
import AlertView from "./alertview/alertView";
import Menu from "./menu";

const client = new ApolloClient({
  uri: "http://localhost:8080/graphql"
});

function App() {
  return (
    <ApolloProvider client={client}>
      <Router>
          <Menu/>
          <Route exact path="/" component={Home} />
          <Route path="/alertview/:id" component={AlertView} />
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
