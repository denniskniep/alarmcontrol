import React, {Component} from 'react';
import {Col, Container,DropdownButton, Dropdown, Row} from "react-bootstrap";
import {gql} from "apollo-boost";
import QueryHandler from "../../utils/queryHandler";
import AaoEditMutation from "./aaoEditMutation";


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

class AaosByOrganisationView extends Component {

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
                                    <h2>Aaos</h2>

                                    <AaoEditMutation
                                        onAaoRulesChanged={() => refetch()}
                                        organisationId={this.props.match.params.id}
                                        /*contacts={mailContacts}*/
                                    />

                                </Col>
                            </Row>
                        </Container>)
                }}
            </QueryHandler>
        )
    }
}

export default AaosByOrganisationView;