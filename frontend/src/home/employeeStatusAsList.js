import {gql} from "apollo-boost";
import {Col, Container, Row, Table} from "react-bootstrap";
import React, {Component} from 'react';
import {Query} from "react-apollo";
import QueryDefaultHandler from "../utils/queryDefaultHandler";
import EmployeeStates from "./employeeStates";
import AlertViewBox from "../alertview/alertViewBox";
import {CurrentOrganisationContext} from "../currentOrganisationContext";

const ORGANISATION_BY_ID = gql`
  query organisationById($organisationId: ID!) {
    organisationById(organisationId: $organisationId) {
      id
      employees {
        id
        firstname
        lastname
        status {
          status
          dateTime
        }
      }
    }
  }
`;

class EmployeeStatusAsList extends Component {

  mapClassForFeedback(feedback) {
    switch (feedback) {
      case EmployeeStates.getNotAvailable():
        return "dot-not-available"
      case EmployeeStates.getAvailable():
        return "dot-available"
      default:
        return null;
    }
  }

  render() {
    return (
        <React.Fragment>
          <CurrentOrganisationContext.Consumer>
            {organisationContext => {
              return (
                  <AlertViewBox>
                    <Container fluid="true"
                               className={"d-flex flex-column h-100"}>
                      <Query query={ORGANISATION_BY_ID}
                             fetchPolicy="no-cache"
                             variables={{organisationId: organisationContext.organisationId}}>
                        {({loading, error, data, refetch}) => {
                          let result = new QueryDefaultHandler().handleGraphQlQuery(
                              loading,
                              error,
                              data,
                              data && data.organisationById);

                          if (result) {
                            return result;
                          }

                          let organisation = data.organisationById;
                          return (
                              <Row>
                                <Col className={"noPadding"}>
                                  <Table responsive>
                                    <tbody>
                                    {organisation.employees.map((e, index) => {
                                      return (
                                          <tr key={e.id}>
                                            <td className={"dot-td"}>
                                              <span
                                                  className={"dot dot-td-container "
                                                  + this.mapClassForFeedback(
                                                      e.status
                                                      && e.status.status)}></span>
                                            </td>
                                            <td>{e.firstname} {e.lastname}</td>
                                          </tr>
                                      )
                                    })}
                                    </tbody>
                                  </Table>
                                </Col>
                              </Row>)
                        }}
                      </Query>
                    </Container>
                  </AlertViewBox>
              );
            }}
          </CurrentOrganisationContext.Consumer>
        </React.Fragment>);
  }
}

export default EmployeeStatusAsList