import {Mutation, Query} from "react-apollo";
import React, {Component} from 'react';
import {gql} from "apollo-boost";
import OrganisationsEdit from "./organisationsEdit";

const ORGANISATIONS = gql`
  query organisations {
    organisations {
      id
      name
    }
  }  
`;

const NEW_ORGANISATION = gql`
    mutation newOrganisation($name: String, $addressLat: String, $addressLng: String) {
     newOrganisation(name: $name, addressLat:  $addressLat, addressLng: $addressLng) {
      id
    }
  }
`;

const DELETE_ORGANISATION = gql`
    mutation deleteOrganisation($id: ID) {
     deleteOrganisation(id: $id)
  }
`;

class OrganisationsView extends Component {
  render() {
    return (
        <Query query={ORGANISATIONS}>
          {({loading, error, data, refetch}) => {
            if (loading) {
              return <p>Loading...</p>;
            }
            if (error) {
              return <p>Error: ${error.message}</p>;
            }
            if (!data.organisations) {
              return <p>NO DATA</p>;
            }

            return (
                <Mutation mutation={NEW_ORGANISATION}
                          onCompleted={() => refetch()}>
                  {createNewOrganisation => (

                      <Mutation mutation={DELETE_ORGANISATION}
                                onCompleted={() => refetch()}>
                        {deleteOrganisation => (

                            <OrganisationsEdit
                                organisations={data.organisations}

                                onNewOrganisation={newOrganisation => {
                                  createNewOrganisation({
                                    variables: {
                                      name: newOrganisation.name,
                                      addressLat: "",
                                      addressLng: ""
                                    }
                                  });
                                }}

                                onOrganisationDeleted={deletedOrganisation => {
                                  deleteOrganisation({
                                    variables: {
                                      id: deletedOrganisation.id
                                    }
                                  });
                                }}

                            />
                        )}
                      </Mutation>

                  )}
                </Mutation>
            );
          }}
        </Query>);
  }
}

export default OrganisationsView;