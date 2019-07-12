import {Mutation, Query} from "react-apollo";
import React, {Component} from 'react';
import {gql} from "apollo-boost";
import OrganisationsEdit from "./organisationsEdit";
import ErrorHandler from "../utils/errorHandler";
import QueryDefaultHandler from "../utils/queryDefaultHandler";

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

class OrganisationsView extends Component {
  render() {
    return (
        <Query fetchPolicy="no-cache" query={ORGANISATIONS}>
          {({loading, error, data, refetch}) => {
            let result = new QueryDefaultHandler().handleGraphQlQuery(loading,
                error,
                data,
                data && data.organisations);

            if(result){
              return result;
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
        </Query>);
  }
}

export default OrganisationsView;