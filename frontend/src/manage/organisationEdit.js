import React, {Component} from 'react';
import {Button, Col, Container, Form, Row} from "react-bootstrap";

class OrganisationEdit extends Component {

  constructor(props) {
    super(props);

    this.state = {
      name: this.props.organisation.name,
      addressLat: this.props.organisation.addressLat,
      addressLng: this.props.organisation.addressLng,
    }
  }

  render() {
    return (
        <Container>
          <Row>
            <Col>
              <Form>
                <Form.Group>
                  <Form.Label>Name</Form.Label>
                  <Form.Control type="text"
                                onChange={ e => this.setState({ name : e.target.value})}
                                value={this.state.name}/>
                </Form.Group>

                <Form.Group>
                  <Form.Label>Breitengrad</Form.Label>
                  <Form.Control type="text"
                                onChange={ e => this.setState({ addressLat : e.target.value})}
                                value={this.state.addressLat}/>
                  <Form.Text className="text-muted">
                    Breitengrad der Koordinate von der
                    Organisation
                  </Form.Text>
                </Form.Group>

                <Form.Group>
                  <Form.Label>Längengrad</Form.Label>
                  <Form.Control type="text"
                                onChange={ e => this.setState({ addressLng : e.target.value})}
                                value={this.state.addressLng}/>
                  <Form.Text className="text-muted">
                    Längengrad der Koordinate von der Organisation
                  </Form.Text>
                </Form.Group>

                <Button variant="success" onClick={ e =>
                    this.props.onSave(this.state)} >
                  Speichern
                </Button>
              </Form>
            </Col>
          </Row>
        </Container>)
  }
}

export default OrganisationEdit;