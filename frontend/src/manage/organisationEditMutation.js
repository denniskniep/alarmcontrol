import React, {Component} from 'react';
import {gql} from "apollo-boost";
import OrganisationEdit from "./organisationEdit";
import QueryHandler from "../utils/queryHandler";
import MutationHandler from "../utils/mutationHandler";

const ORGANISATION_BY_ID = gql`
  query organisationById($id: ID!) {
    organisationById(organisationId: $id) {
      id
      name
      addressLat
      addressLng
    }
  }
`;

const UPDATE_ORGANISATION = gql`
    mutation editOrganisation($id: ID!, $name: String!, $addressLat: String!, $addressLng: String!) {
     editOrganisation(id: $id, name: $name, addressLat: $addressLat, addressLng: $addressLng) {
      id
    }
  }
`;

class OrganisationEditMutation extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <QueryHandler fetchPolicy="no-cache" query={ORGANISATION_BY_ID}
               variables={{id: this.props.id}}>
          {({data, refetch}) => {

            if (data && !data.organisationById) {
              return <React.Fragment></React.Fragment>;
            }

            return (
                <MutationHandler mutation={UPDATE_ORGANISATION}
                                 onCompleted={() => refetch()}>
                  {updateOrganisation => (

                      <OrganisationEdit organisation={data.organisationById}
                                        onSave={org =>
                                            updateOrganisation({
                                                  variables: {
                                                    id: this.props.id,
                                                    name: org.name,
                                                    addressLng: org.addressLng,
                                                    addressLat: org.addressLat
                                                  }
                                                }
                                            )}/>

                  )}
                </MutationHandler>
            );
          }}
        </QueryHandler>);
  }
}

export default OrganisationEditMutation;