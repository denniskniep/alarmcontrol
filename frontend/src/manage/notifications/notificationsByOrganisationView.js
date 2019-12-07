import React, {Component} from 'react';
import {Col, Container,DropdownButton, Dropdown, Row} from "react-bootstrap";
import {gql} from "apollo-boost";
import QueryHandler from "../../utils/queryHandler";
import MailContactsEditMutation from "./mailContactsEditMutation";
import NotificationSubscriptionsEditMutation from "./notificationSubscriptionsEditMutation";
import EditableTable from "../../components/editableTable";

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