import React, {Component} from 'react';
import {Col, Container, Row} from "react-bootstrap";
import AlertViewBox from "../alertViewBox";
import {gql} from "apollo-boost";
import {Query} from "react-apollo";
import EmployeeFeedbackAsBadges from "./employee/employeeFeedbackAsBadges";
import EmployeeFeedbackStates from "./employee/employeeFeedbackStates";
import EmployeeFeedbackAsList from "./employee/employeeFeedbackAsList";
import EmployeeFeedbackAggregated from "./employee/employeeFeedbackAggregated";

const ALERT_WITH_EMPLOYEE_BY_ID = gql`
  query alertById($id: ID) {
    alertById(id: $id) {
      id    
      employeeFeedback{
        feedback
        employee{
          id,
          firstname,
          lastname,
          skills{
            id
            shortName
            displayAtOverview
          }
        }
      }
    }
  }
`;

const SKILLS_BY_ORGANISATION_ID = gql`
query skillsByOrganisationId($id: ID){
  skillsByOrganisationId(organisationId: $id) {
    id,
    shortName
    displayAtOverview
  }
}
`;

class AlertViewEmployeeFeedback extends Component {

  filterAndSortByFeedback(employeeFeedback, filterAndOrder) {
    let filtered = employeeFeedback.filter(
        ef => filterAndOrder.includes(ef.feedback));

    let ordered = filtered.sort(function (a, b) {
      let aIndex = filterAndOrder.indexOf(a.feedback);
      let bIndex = filterAndOrder.indexOf(b.feedback);
      return aIndex - bIndex
    });

    return ordered;
  }

  render() {
    return (
        <Query query={SKILLS_BY_ORGANISATION_ID}
               variables={{id: this.props.alert.organisation.id}}>

          {({loading, error, data}) => {
            if (loading || error || !data || !data.skillsByOrganisationId) {
              return <p></p>;
            }

            let skills = data.skillsByOrganisationId;

            return (
                <Query query={ALERT_WITH_EMPLOYEE_BY_ID}
                       variables={{id: this.props.alert.id}}>
                  {({loading, error, data}) => {
                    if (loading || error || !data.alertById) {
                      return <p></p>;
                    }

                    let hasEmployeeThatAreLater = this.filterAndSortByFeedback(
                        data.alertById.employeeFeedback, [
                          EmployeeFeedbackStates.getLater()
                        ]).length > 0

                    return (
                        <AlertViewBox>
                          <Container fluid="true"
                                     className={"d-flex flex-column h-100"}>

                            <EmployeeFeedbackAggregated
                                icon={["far", "check-circle"]}
                                badgeVariant="success"
                                skills={skills}
                                employeeFeedback={
                                  this.filterAndSortByFeedback(
                                      data.alertById.employeeFeedback, [
                                        EmployeeFeedbackStates.getCommit()
                                      ])
                                }
                            />

                            <EmployeeFeedbackAsList
                                employeeFeedback={
                                  this.filterAndSortByFeedback(
                                      data.alertById.employeeFeedback, [
                                        EmployeeFeedbackStates.getCommit()
                                      ])
                                }
                            />

                            <Row>
                              <Col>
                                <hr/>
                              </Col>
                            </Row>


                            {hasEmployeeThatAreLater &&
                            <EmployeeFeedbackAggregated
                                icon={["far", "clock"]}
                                badgeVariant="info"
                                skills={skills}
                                employeeFeedback={
                                  this.filterAndSortByFeedback(
                                      data.alertById.employeeFeedback, [
                                        EmployeeFeedbackStates.getLater()
                                      ])
                                }
                            />}

                            {hasEmployeeThatAreLater &&
                            <EmployeeFeedbackAsList
                                employeeFeedback={
                                  this.filterAndSortByFeedback(
                                      data.alertById.employeeFeedback, [
                                        EmployeeFeedbackStates.getLater()
                                      ])
                                }
                            />}

                            {hasEmployeeThatAreLater &&
                            <Row>
                              <Col>
                                <hr/>
                              </Col>
                            </Row>
                            }

                            <EmployeeFeedbackAsBadges
                                employeeFeedback={
                                  this.filterAndSortByFeedback(
                                      data.alertById.employeeFeedback, [
                                        EmployeeFeedbackStates.getNoResponse(),
                                        EmployeeFeedbackStates.getCancel()
                                      ])
                                }
                            />

                          </Container>
                        </AlertViewBox>);
                  }}
                </Query>)
          }}
        </Query>);
    ;
  }
}

export default AlertViewEmployeeFeedback