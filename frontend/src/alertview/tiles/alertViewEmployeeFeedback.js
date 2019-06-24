import React, {Component} from 'react';
import {Col, Container, Row} from "react-bootstrap";
import AlertViewBox from "../alertViewBox";
import {gql} from "apollo-boost";
import {Query, Subscription} from "react-apollo";
import EmployeeFeedbackAsBadges from "./employee/employeeFeedbackAsBadges";
import EmployeeFeedbackStates from "./employee/employeeFeedbackStates";
import EmployeeFeedbackAsList from "./employee/employeeFeedbackAsList";
import EmployeeFeedbackAggregated from "./employee/employeeFeedbackAggregated";

const ALERT_WITH_EMPLOYEE_BY_ID = gql`
  query alertById($id: ID) {
    alertById(id: $id) {
      id    
      organisation{
        skills{
          id,
          shortName
          displayAtOverview
        }
      },
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

const ALERT_EMPLOYEE_FEEDBACK_ADDED = gql`
  subscription employeeFeedbackAdded{
    employeeFeedbackAdded {
      alertId
      alertCallId
      employeeId
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
        <React.Fragment>
          <Query fetchPolicy="no-cache" query={ALERT_WITH_EMPLOYEE_BY_ID}
                 variables={{id: this.props.alert.id}}>
            {({loading, error, data, refetch}) => {
              if (loading || error || !data.alertById) {
                return <p></p>;
              }

              let alertData = data;
              let hasEmployeeThatAreLater = this.filterAndSortByFeedback(
                  alertData.alertById.employeeFeedback, [
                    EmployeeFeedbackStates.getLater()
                  ]).length > 0

              let skills = alertData.alertById.organisation.skills;

              return (
                  <Subscription fetchPolicy="no-cache"
                                subscription={ALERT_EMPLOYEE_FEEDBACK_ADDED}>
                    {({loading, error, data}) => {

                      let subscriptionData = data;
                      if (subscriptionData
                          && subscriptionData.employeeFeedbackAdded) {
                        refetch();
                      }

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
                                        alertData.alertById.employeeFeedback,
                                        [
                                          EmployeeFeedbackStates.getCommit()
                                        ])
                                  }
                              />

                              <EmployeeFeedbackAsList
                                  employeeFeedback={
                                    this.filterAndSortByFeedback(
                                        alertData.alertById.employeeFeedback,
                                        [
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
                                        alertData.alertById.employeeFeedback,
                                        [
                                          EmployeeFeedbackStates.getLater()
                                        ])
                                  }
                              />}

                              {hasEmployeeThatAreLater &&
                              <EmployeeFeedbackAsList
                                  employeeFeedback={
                                    this.filterAndSortByFeedback(
                                        alertData.alertById.employeeFeedback,
                                        [
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
                                        alertData.alertById.employeeFeedback,
                                        [
                                          EmployeeFeedbackStates.getNoResponse(),
                                          EmployeeFeedbackStates.getCancel()
                                        ])
                                  }
                              />

                            </Container>
                          </AlertViewBox>
                      );
                    }}
                  </Subscription>
              )
            }}
          </Query>
        </React.Fragment>
    )
  }
}

export default AlertViewEmployeeFeedback