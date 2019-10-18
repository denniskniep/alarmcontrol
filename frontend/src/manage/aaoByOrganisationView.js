import React, {Component} from 'react';
import {Col, Container, Row} from "react-bootstrap";
import AaosEditMutation from "./aaosEditMutation"
class AaoByOrganisationView extends Component {
    render() {

        return (
            <Container>
                <Row className={"row-header"}>
                    <Col>
                        <h2>AAO</h2>
                    </Col>
                </Row>

                <Row>
                    <Col>
                        <AaosEditMutation/>
                    </Col>
                </Row>
            </Container>)
    }
}

export default AaoByOrganisationView;

//console.log('xxx',this.props.match.params.id);