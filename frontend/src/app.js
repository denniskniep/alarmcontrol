import React from "react";
import { BrowserRouter as Router, Route, NavLink } from "react-router-dom";
import { ApolloProvider } from "react-apollo";
import ApolloClient from "apollo-boost";
import { Navbar, Nav } from 'react-bootstrap';
import AlertView from "./alertview/alertView";

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

function Menu() {
  return (
      <Navbar sticky="top" collapseOnSelect expand="lg" bg="dark" variant="dark">
        <Navbar.Brand>AlarmControl</Navbar.Brand>
        <Navbar.Toggle aria-controls="responsive-navbar-nav" />
        <Navbar.Collapse id="responsive-navbar-nav">
          <Nav className="mr-auto">
            <NavLink className={"nav-link"} exact to="/">Home</NavLink>
            <NavLink className={"nav-link"} to="/alertview/2">Alert 2</NavLink>
            <NavLink className={"nav-link"} to="/alertview/3">Alert 3</NavLink>
            <NavLink className={"nav-link"} to="/alertview/4">Alert 4</NavLink>
            <NavLink className={"nav-link"} to="/alertview/5">Alert 5</NavLink>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
  );
}

export default App;
