import {Mutation} from "react-apollo";
import React, {Component} from 'react';
import {gql} from "apollo-boost";
import OrganisationsEdit from "./organisationsEdit";
import ErrorHandler from "../utils/errorHandler";
import QueryHandler from "../utils/queryHandler";

const ORGANISATIONS = gql`
  query organisations {
    organisations {
      id
      name
    }
  }  
`;

const NEW_ORGANISATION = gql`
    mutation newOrganisation($name: String!, $addressLat: String!, $addressLng: String!) {
     newOrganisation(name: $name, addressLat:  $addressLat, addressLng: $addressLng) {
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
                <Mutation mutation={NEW_ORGANISATION}

                          onError={(error) =>
                              new ErrorHandler().handleGraphQlMutationError(error)}

                          onCompleted={() => refetch()}>
                  {createNewOrganisation => (

                      <Mutation mutation={DELETE_ORGANISATION}

                                onError={(error) =>
                                    new ErrorHandler().handleGraphQlMutationError(error)}

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
        </QueryHandler>);
  }
}

export default OrganisationsEditMutation;