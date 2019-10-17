import React, {Component} from 'react';
import {
  Button,
  ButtonToolbar,
  Card,
  Col,
  Container,
  Row
} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import SubscriberEdit from "./subscriberEdit";
import {gql} from "apollo-boost";
import MutationHandler from "../../utils/mutationHandler";

const DELETE_NOTIFICATION_SUBSCRIPTION = gql`
mutation deleteNotificationSubscription($organisationId: ID!, $uniqueSubscriptionId: String!){
  deleteNotificationSubscription(organisationId: $organisationId, uniqueSubscriptionId: $uniqueSubscriptionId)
}
`;

class NotificationSubscriptionEditMutation extends Component {

  constructor(props) {
    super(props);

    this.state = {
      variablesForGQLEdit: this.props.subscription.notificationConfig,
      receiver: this.props.subscription.subscriberContactUniqueIds,
      changedAnything: false
    }
  }

  edit(subscription, editNotificationSubscription) {
    editNotificationSubscription({
      variables: {
        organisationId: this.props.organisationId,
        uniqueSubscriptionId: this.props.subscription.uniqueId,
        subscriberContactUniqueIds: this.state.receiver,
        ...this.state.variablesForGQLEdit
      }
    });
  }

  delete(subscription, deleteNotificationSubscription) {
    deleteNotificationSubscription({
      variables: {
        organisationId: this.props.organisationId,
        uniqueSubscriptionId: subscription.uniqueId
      }
    });
  }

  handleVariablesForGQLEditChanged(variablesForGQLEdit) {
    this.setState((state, props) => {
      return {variablesForGQLEdit: variablesForGQLEdit, changedAnything: true};
    });
  }

  render() {
    return (<MutationHandler mutation={DELETE_NOTIFICATION_SUBSCRIPTION}
                             onCompleted={() =>
                                 this.props.onSubscriptionChanged
                                 && this.props.onSubscriptionChanged()
                             }>
          {deleteNotificationSubscription => {
            return (
                <MutationHandler
                    mutation={this.props.editNotificationSubscriptionGQLQuery}
                    onCompleted={() => {
                      this.props.onSubscriptionChanged
                      && this.props.onSubscriptionChanged()

                      this.setState((state, props) => {
                        return {changedAnything: false};
                      });
                    }}>
                  {editNotificationSubscription => {
                    return (
                        <React.Fragment>
                          <Card>
                            <Card.Header>
                              <div>
                                <h4 className={"float-left"}>
                                  {this.props.title}
                                </h4>
                                { this.state.changedAnything &&
                                  <span className={"left-to-h4 float-left"}><i>(Nicht gespeicherte Änderungen) </i></span>
                                }
                              </div>
                              <div className={"float-right"}>
                                <ButtonToolbar>
                                  <Button className={"btn-icon"}
                                          variant="success"
                                          disabled={!this.state.changedAnything}
                                          onClick={e =>
                                              this.edit(
                                                  this.props.subscription,
                                                  editNotificationSubscription)}>
                                    <FontAwesomeIcon icon={["far", "save"]}/>
                                  </Button>
                                  <Button className={"btn-icon btn-icon-space"}
                                          variant="outline-secondary"
                                          onClick={e => this.delete(
                                              this.props.subscription,
                                              deleteNotificationSubscription)}>
                                    <FontAwesomeIcon
                                        icon={["far", "trash-alt"]}/>
                                  </Button>
                                </ButtonToolbar>
                              </div>
                            </Card.Header>
                            <Card.Body>
                              <Container>
                                <Row>
                                  <Col style={{paddingBottom: "20px"}}>
                                    <b>Empfänger</b>
                                    <SubscriberEdit
                                        contacts={this.props.contacts}
                                        receiver={this.state.receiver}
                                        onChange={receiver => {
                                          this.setState((state, props) => {
                                            return {
                                              receiver: receiver,
                                              changedAnything: true
                                            };
                                          });
                                        }}
                                    />
                                  </Col>
                                </Row>
                                {
                                  React.cloneElement(this.props.children, {
                                    values: this.state.variablesForGQLEdit,
                                    onValuesChanged: (values) => this.handleVariablesForGQLEditChanged(
                                        values)
                                  })
                                }
                              </Container>
                            </Card.Body>
                          </Card>
                        </React.Fragment>
                    )
                  }}</MutationHandler>
            )
          }}</MutationHandler>
    )
  }
}

export default NotificationSubscriptionEditMutation;