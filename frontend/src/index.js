import React from "react";
import ReactDOM from "react-dom";
import ApolloClient from "apollo-boost";
import { gql } from "apollo-boost";

const client = new ApolloClient({
  uri: "http://localhost:8080/graphql"
});

class HelloMessage extends React.Component {

  constructor(props, context) {
    super(props, context);

    this.state = {
      greeting: ""
    };
  }

  componentDidMount() {
    client
    .query({
      query: gql`
      query {
        alertById(id: 1) {
          id
          keyword
          dateTime
        }
      }
    `
    })
    .then(result => console.log(result));
  }

  render() {
    return <div>{this.state.greeting} {this.props.name}</div>;
  }
}

const mountNode = document.getElementById("app");
ReactDOM.render(<HelloMessage name="Alert" />, mountNode);
