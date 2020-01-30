import React, {Component} from 'react';
import {gql} from "apollo-boost";
import OrganisationsEdit from "./organisationsEdit";
import QueryHandler from "../utils/queryHandler";
import MutationHandler from "../utils/mutationHandler";

const ORGANISATIONS = gql`
  query organisations {
    organisations {
      id
      name
    }
  }  
`;

const NEW_ORGANISATION = gql`
    mutation newOrganisation($name: String!, $addressLat: String!, $addressLng: String!, $location: String!) {
     newOrganisation(name: $name, addressLat:  $addressLat, addressLng: $addressLng, location: $location) {
      id
    }
  }
`;

const DELETE_ORGANISATION = gql`
    mutation deleteOrganisation($id: ID!) {
     deleteOrganisation(id: $id)
  }
`;

class OrganisationsEditMutation extends Component {
  render() {
    return (
        <QueryHandler fetchPolicy="no-cache" query={ORGANISATIONS}>
          {({data, refetch}) => {
            if (data && !data.organisations) {
              return <React.Fragment></React.Fragment>;
            }

            return (
                <MutationHandler mutation={NEW_ORGANISATION}
                                 onCompleted={() => refetch()}>
                  {createNewOrganisation => (

                      <MutationHandler mutation={DELETE_ORGANISATION}
                                       onCompleted={() => refetch()}>
                        {deleteOrganisation => (

                            <OrganisationsEdit
                                organisations={data.organisations}

                                onNewOrganisation={newOrganisation => {
                                  createNewOrganisation({
                                    variables: {
                                      name: newOrganisation.name,
                                      addressLat: "",
                                      addressLng: "",
                                      location: ""
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
                      </MutationHandler>

                  )}
                </MutationHandler>
            );
          }}
        </QueryHandler>);
  }
}

export default OrganisationsEditMutation;