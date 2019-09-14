import {Mutation} from "react-apollo";
import React, {Component} from 'react';
import {gql} from "apollo-boost";
import OrganisationEdit from "./organisationEdit";
import ErrorHandler from "../utils/errorHandler";
import QueryHandler from "../utils/queryHandler";

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
                <Mutation mutation={UPDATE_ORGANISATION}

                          onError={(error) =>
                              new ErrorHandler().handleGraphQlMutationError(error)}

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
        </QueryHandler>);
  }
}

export default OrganisationEditMutation;