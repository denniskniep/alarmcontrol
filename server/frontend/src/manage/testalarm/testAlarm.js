import React, {Component} from 'react';
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import configuration from "../../config/configuration";
import {gql} from "apollo-boost";
import QueryHandler from "../../utils/queryHandler";

const ORGANISATIONS = gql`
  query organisations {
    organisations {
      id,
      name
    }
  }
`;

class TestAlarm extends Component {

  constructor(props) {
    super(props);

    this.state = {
      organisationId: 1,
      gssi: "123456",
      id: "321",
      text: "&54S54*STELLEN SIE EINSATZBEREITSCHAFT HER B123456778*H1*FÜRSTENWALD HINTER DEN GÄRTEN 8 CALDEN",
      response: ""
    }
  }

  sendTestAlarm() {
    let url = "http://" + configuration.getServer() + "/api/alert";

    fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(this.state)
    }).then((response) => response.json())
    .then((data) => {
      console.log('Success:', data);
      this.setState({response: JSON.stringify(data)})
    })
    .catch((error) => {
      console.error('Error:', error);
      this.setState({response: JSON.stringify(error)})
    });
  }

  render() {
    return (

        <QueryHandler query={ORGANISATIONS}
                      fetchPolicy="no-cache">
          {({data}) => {

            if (data && !data.organisations) {
              return <React.Fragment></React.Fragment>;
            }

            let organisations = data.organisations;

            return (

                <Container>

                  <Row className={"row-header"}>
                    <h1>Testalarm</h1>
                  </Row>

                  <Row className={"row-header"}>
                    <Col md={12}>
                      <Form>

                        <Form.Group controlId="formGridState">
                          <Form.Label>Organisation:</Form.Label>
                          <Form.Control as="select"
                                        value={this.state.organisationId}
                                        onChange={
                                          e => this.setState(
                                              {organisationId: e.target.value})}>
                            {
                              organisations.map(
                                  (organisation) => {
                                    return (
                                        <option
                                            key={organisation.id}
                                            value={organisation.id}
                                        >
                                          {organisation.name}
                                        </option>)
                                  })
                            }
                          </Form.Control>
                        </Form.Group>


                        <Form.Group>
                          <Form.Label>GSSI</Form.Label>
                          <Form.Control type="text"
                                        value={this.state.gssi}
                                        onChange={e => this.setState(
                                            {gssi: e.target.value})}/>
                        </Form.Group>

                        <Form.Group>
                          <Form.Label>ID</Form.Label>
                          <Form.Control type="text"
                                        value={this.state.id}
                                        onChange={e => this.setState(
                                            {id: e.target.value})}/>
                        </Form.Group>

                        <Form.Group>
                          <Form.Label>Text</Form.Label>
                          <Form.Control as="textarea" rows="3"
                                        value={this.state.text}
                                        onChange={e => this.setState(
                                            {text: e.target.value})}/>
                        </Form.Group>

                        <Button variant="primary"
                                onClick={() => this.sendTestAlarm()}>
                          Send
                        </Button>
                      </Form>
                    </Col>
                  </Row>
                  <Row>{this.state.response &&
                  <React.Fragment>
                    <p><b>Response from Server:</b></p>
                    <p>{this.state.response}</p>
                  </React.Fragment>
                  }
                  </Row>
                </Container>
            )
          }}</QueryHandler>
    )
  }
}

export default TestAlarm;