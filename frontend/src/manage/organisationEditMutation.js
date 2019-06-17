import {Mutation, Query} from "react-apollo";
import React, {Component} from 'react';
import {gql} from "apollo-boost";
import OrganisationEdit from "./organisationEdit";

const ORGANISATION_BY_ID = gql`
  query organisationById($id: ID) {
    organisationById(organisationId: $id) {
      id
      name
      addressLat
      addressLng
    }
  }
`;

const UPDATE_ORGANISATION = gql`
    mutation editOrganisation($id: ID, $name: String, $addressLat: String, $addressLng: String) {
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
        <Query fetchPolicy="no-cache" query={ORGANISATION_BY_ID}
               variables={{id: this.props.id}}>
          {({loading, error, data, refetch}) => {
            if (loading) {
              return <p>Loading...</p>;
            }
            if (error) {
              return <p>Error: ${error.message}</p>;
            }

            if (!data.organisationById) {
              return <p>NO DATA</p>;
            }

            return (
                <Mutation mutation={UPDATE_ORGANISATION}
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
                </Mutation>
            );
          }}
        </Query>);
  }
}

export default OrganisationEditMutation;