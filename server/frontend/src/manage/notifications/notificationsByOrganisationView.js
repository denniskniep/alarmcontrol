import React, {Component} from 'react';
import {
  Col,
  Container,
  DropdownButton,
  Dropdown,
  Row,
  Button, Modal
} from "react-bootstrap";
import {gql} from "apollo-boost";
import QueryHandler from "../../utils/queryHandler";
import MailContactsEditMutation from "./contacts/mailContactsEditMutation";
import NotificationSubscriptionsEditMutation from "./notificationSubscriptionsEditMutation";
import FirebaseMessageContactsEditMutation  from "./contacts/firebaseMessageContactsEditMutation";
import EditableTable from "../../components/editableTable";
import FirebaseConfig from "./firebaseConfig";

const NOTIFICATION_CONFIG_BY_ORGANISATION_ID = gql`
query organisationById($id: ID!) {
  organisationById(organisationId: $id) {
    id
    notificationConfig {
      contacts {
        uniqueId
        name
        __typename
        ... on MailContact {
          mailAddress
        }
        ... on FirebaseMessageContact {
          mail
        }
      }
      subscriptions {
        uniqueId
        subscriberContactUniqueIds
        notificationConfig {
          __typename
          ... on AlertCreatedNotificationConfig {
            updateDelaysInSeconds
          }
        }
      }
    }
  }
}
`;

class NotificationsByOrganisationView extends Component {

  constructor(props) {
    super(props);

    this.state = {
      showFirebaseConfig: false
    }
  }

  handleFirebaseConfigClose(){
    this.setState((state, props) => {
      return {
        showFirebaseConfig: false
      }
    });
  }

  handleFirebaseConfigOpen(){
    this.setState((state, props) => {
      return {
        showFirebaseConfig: true
      }
    });
  }

  render() {
    return (
        <QueryHandler fetchPolicy="no-cache"
                      query={NOTIFICATION_CONFIG_BY_ORGANISATION_ID}
                      variables={{
                        id: this.props.match.params.id
                      }}>
          {({data, refetch}) => {

            if (data && !data.organisationById) {
              return <React.Fragment></React.Fragment>;
            }


            let mailContacts = data
            .organisationById
            .notificationConfig
            .contacts
            .filter(contact => contact.__typename == "MailContact");

            let firebaseMessageContacts = data
            .organisationById
            .notificationConfig
            .contacts
            .filter(contact => contact.__typename == "FirebaseMessageContact");

            return (
                <Container>
                  <Row className={"row-header"}>
                    <Col>
                      <h2>E-Mails</h2>

                      <MailContactsEditMutation
                          onContactsChanged={() => refetch()}
                          organisationId={this.props.match.params.id}
                          contacts={mailContacts}
                      />

                       <div className={"float-left"}>
                         <h2>Firebase Push</h2>
                       </div>
                      <div className={"float-left"} style={{marginLeft: "15px"}}>
                        <Button variant="primary" onClick={() => this.handleFirebaseConfigOpen()}>
                          View Pager Config
                        </Button>
                      </div>

                      <Modal show={this.state.showFirebaseConfig} onHide={() => this.handleFirebaseConfigClose()}>
                        <Modal.Header closeButton>
                          <Modal.Title>Config for Pager</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                          <FirebaseConfig/>
                        </Modal.Body>
                        <Modal.Footer>
                          <Button variant="secondary" onClick={() => this.handleFirebaseConfigClose()}>
                            Close
                          </Button>
                        </Modal.Footer>
                      </Modal>

                      <FirebaseMessageContactsEditMutation
                          onContactsChanged={() => refetch()}
                          organisationId={this.props.match.params.id}
                          contacts={firebaseMessageContacts}
                        />

                    </Col>
                  </Row>

                  <Row className={"row-header"}>
                    <Col>
                      <NotificationSubscriptionsEditMutation
                          organisationId={data.organisationById.id}
                          contacts={data.organisationById.notificationConfig.contacts}
                          subscriptions={data.organisationById.notificationConfig.subscriptions}
                          onSubscriptionChanged={() => refetch()}
                      />
                    </Col>
                  </Row>
                </Container>)
          }}
        </QueryHandler>
    )
  }
}

export default NotificationsByOrganisationView;
