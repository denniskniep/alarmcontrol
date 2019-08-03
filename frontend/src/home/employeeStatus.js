import {gql} from "apollo-boost";
import {Col, Container, Row, Table} from "react-bootstrap";
import React, {Component} from 'react';
import {Query} from "react-apollo";
import QueryDefaultHandler from "../utils/queryDefaultHandler";
import AlertViewBox from "../alertview/alertViewBox";
import {CurrentOrganisationContext} from "../currentOrganisationContext";
import EmployeeStatusAddedSubscription from "./employeeStatusAddedSubscription";
import EmployeeStatusAsList from "./employeeStatusAsList";
import EmployeeStatusAggregated from "./employeeStatusAggregated";

const ORGANISATIONS_BY_ID = gql`
  query organisationsById($organisationId: ID) {
    organisationsById(organisationId: $organisationId) {
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

class EmployeeStatus extends Component {

  getValidOrganisationId(organisationId) {
    return organisationId == 0 ? null : organisationId * 1;
  }

  getEmployees(organisations){
    return organisations.reduce((employees, organisation) => employees.concat(organisation.employees), []);
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
                      <Query query={ORGANISATIONS_BY_ID}
                             fetchPolicy="no-cache"
                             variables={{
                               organisationId: this.getValidOrganisationId(
                                   organisationContext.organisationId)
                             }}>
                        {({loading, error, data, refetch}) => {
                          let result = new QueryDefaultHandler().handleGraphQlQuery(
                              loading,
                              error,
                              data,
                              data && data.organisationsById);

                          if (result) {
                            return result;
                          }

                          let organisations = data.organisationsById;
                          return (
                              <React.Fragment>
                                <EmployeeStatusAddedSubscription
                                    onSubscriptionData={(o) => refetch()}/>
                                <EmployeeStatusAggregated employees={this.getEmployees(organisations) }/>
                                <EmployeeStatusAsList employees={this.getEmployees(organisations) }/>

                              </React.Fragment>
                             )
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

export default EmployeeStatus