import React, {Component} from 'react';
import {Col, Container,DropdownButton, Dropdown, Row} from "react-bootstrap";
import {gql} from "apollo-boost";
import QueryHandler from "../../utils/queryHandler";
import AaoEditMutation from "./aaoEditMutation";
import VehicleEditMutation from "./vehicleEditMutation";
import LocationEditMutation from "./locationEditMutation";
import EditableTable from "../../components/editableTable";


const NOTIFICATION_CONFIG_BY_ORGANISATION_ID = gql`
query organisationById($id: ID!) {
  organisationById(organisationId: $id) {
    id
    aaoConfig {
      aaoRules {
        uniqueId
        __typename
        vehicles
        keywords
        locations
      }
      vehicles {
        uniqueId
        name
      }
      locations {
       uniqueId
        name
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


                    let aaoRules = data
                        .organisationById
                        .aaoConfig
                        .aaoRules

                    let vehicles = data
                        .organisationById
                        .aaoConfig
                        .vehicles

                    let storedLocations = data
                        .organisationById
                        .aaoConfig
                        .locations

                    var specialLocations = [{
                        uniqueId: '0',
                        name: 'Meine Ortschaft',
                        canDelete : false
                    },
                    {
                        uniqueId: '00',
                        name: 'Andere Ortschaften',
                        canDelete: false
                    }];
                    let locations = specialLocations.concat(storedLocations)

                    let catalogs= [{ keywordcatalog : 'Hessische Einsatzstichworte für Brandeinsätze' }];

                    return (
                        <Container>
                            <Row className={"row-header"}>
                                <Col>
                                    <h2>Alarmstichworte</h2>
                                    <EditableTable data={catalogs}
                                                   canView={false}
                                                   canEdit={false}
                                                   canDelete={false}
                                                   canCreate={false}
                                                   columns={[
                                                       {
                                                           key: "keywordcatalog",
                                                           name: "Katalog"
                                                       }
                                                   ]}
                                    />

                                </Col>
                            </Row>
                            <Row className={"row-header"}>
                                <Col>
                                    <h2>Fahrzeuge</h2>

                                    <VehicleEditMutation
                                        onVehiclesChanged={() => refetch()}
                                        organisationId={this.props.match.params.id}
                                        vehicles={vehicles}
                                    />
                                </Col>
                            </Row>
                            <Row className={"row-header"}>
                                <Col>
                                    <h2>Ortschaften</h2>

                                    <LocationEditMutation
                                        onLocationsChanged={() => refetch()}
                                        organisationId={this.props.match.params.id}
                                        locations={locations}
                                    />
                                </Col>
                            </Row>
                            <Row className={"row-header"}>
                                <Col>
                                    <h2>Aaos</h2>

                                    <AaoEditMutation
                                        onAaoRulesChanged={() => refetch()}
                                        organisationId={this.props.match.params.id}
                                        aaoRules={aaoRules}
                                        vehicles={vehicles}
                                        locations={locations}
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