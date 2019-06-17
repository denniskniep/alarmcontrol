import {Query} from "react-apollo";
import React, {Component} from 'react';
import {gql} from "apollo-boost";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
  Container,
  Col,
  Row,
  Table,
  ButtonToolbar,
  Button, ButtonGroup
} from "react-bootstrap";


const ORGANISATIONS = gql`
  query organisations {
    organisations {
      id
      name
    }
  }
`;

class OrganisationsView extends Component {
  render() {
    return (
        <Query query={ORGANISATIONS}>
          {({loading, error, data}) => {
            if (loading) {
              return <p>Loading...</p>;
            }
            if (error) {
              return <p>Error: ${error.message}</p>;
            }
            if (!data.organisations) {
              return <p>NO DATA</p>;
            }

            return ( <Container>
                  <Row className={"row-header"}>
                    <Col md="10">
                      <h2>Organisations</h2>
                    </Col>
                    <Col md="2">
                      <Button size={"sm"} variant="success">Erstellen</Button>
                    </Col>
                  </Row>

                  <Row>
                    <Col>
                      <Table>
                        <thead>
                          <tr>
                            <td>#</td>
                            <td>Name</td>
                            <td> </td>
                          </tr>
                        </thead>
                        <tbody>
                        {data.organisations.map((org, index) => {
                          return (
                              <tr>
                                <td>{org.id}</td>
                                <td>{org.name}</td>
                                <td>
                                  <ButtonToolbar>
                                    <ButtonGroup className="mr-2">
                                      <Button className={"btn-icon"} variant="outline-secondary" href={"/manage/organisation/" + org.id}>
                                        <FontAwesomeIcon icon={["far", "edit"]}/>
                                      </Button>
                                      <Button className={"btn-icon"} variant="outline-secondary" href={"/manage/organisation/delete/" + org.id}>
                                        <FontAwesomeIcon icon={["far", "trash-alt"]}/>
                                      </Button>
                                    </ButtonGroup>
                                  </ButtonToolbar>
                                </td>
                              </tr>
                          )
                        })}
                        </tbody>
                      </Table>
                    </Col>
                  </Row>
                </Container>
            );
          }}
        </Query>);
  }
}

export default OrganisationsView;