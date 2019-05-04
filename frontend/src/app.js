import React from "react";
import AlertView from "./alertView";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import { ApolloProvider } from "react-apollo";
import ApolloClient from "apollo-boost";

const client = new ApolloClient({
  uri: "http://localhost:8080/graphql"
});

function App() {
  return (
    <ApolloProvider client={client}>
      <Router>
        <div>
          <Menu/>
          <Route exact path="/" component={Home} />
          <Route path="/alertview/:id" component={AlertView} />
        </div>
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

function Menu() {
  return (
      <div>
        <ul>
          <li>
            <Link to="/">Home</Link>
          </li>
          <li>
            <Link to="/alertview/3">Alert Nr. 3</Link>
          </li>
          <li>
            <Link to="/alertview/2">Alert Nr. 2</Link>
          </li>
        </ul>
        <hr />
      </div>
  );
}

export default App;
