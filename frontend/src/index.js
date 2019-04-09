import React from "react";
import ReactDOM from "react-dom";

class HelloMessage extends React.Component {

  constructor(props, context) {
    super(props, context);

    this.state = {
      greeting: ""
    };
  }

  componentDidMount() {
    fetch('http://localhost:8080/api/test')
    .then(response =>  response.json())
    .then(resData => {
      this.setState({ greeting: resData.greeting }); //this is an asynchronous function
    })
  }

  render() {
    return <div>{this.state.greeting} {this.props.name}</div>;
  }
}

const mountNode = document.getElementById("app");
ReactDOM.render(<HelloMessage name="Alert" />, mountNode);
