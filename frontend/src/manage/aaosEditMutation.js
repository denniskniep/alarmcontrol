import React, {Component} from 'react';
import {gql} from "apollo-boost";
import AaosEdit from "./aaosEdit";
import QueryHandler from "../utils/queryHandler";
import MutationHandler from "../utils/mutationHandler";
import AaoView from "./aaoView";

const ORGANISATIONS = gql`
  query organisations {
    organisations {
      id
      name
    }
  }  
`;

const NEW_AAO = gql`
    mutation newAao($keywords: String!) {
     newAao(keywords: $keywords) {
      id
    }
  }
`;

const DELETE_ORGANISATION = gql`
    mutation deleteOrganisation($id: ID!) {
     deleteOrganisation(id: $id)
  }
`;

class AaosEditMutation extends Component {
    render() {
        return (
            <QueryHandler fetchPolicy="no-cache" query={ORGANISATIONS}>
                {({data, refetch}) => {
                    if (data && !data.organisations) {
                        return <React.Fragment></React.Fragment>;
                    }

                    return (
                        <MutationHandler mutation={NEW_AAO}
                                         onCompleted={() => refetch()}>
                            {createNewAao => (

                                <MutationHandler mutation={DELETE_ORGANISATION}
                                                 onCompleted={() => refetch()}>
                                    {deleteOrganisation => (

                                        <AaoView
                                            organisations={data.organisations}
                                            onNewAao={newAao => {
                                                console.log('newaao');
                                                createNewAao({
                                                    variables: {
                                                        keywords: newAao.keywords
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

export default AaosEditMutation;