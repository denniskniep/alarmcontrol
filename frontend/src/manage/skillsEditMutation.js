import {Mutation, Query} from "react-apollo";
import React, {Component} from 'react';
import {gql} from "apollo-boost";
import SkillsEdit from "./skillsEdit";

const SKILLS_BY_ORGANISATION_ID = gql`
  query organisationById($id: ID) {
    organisationById(organisationId: $id) {
      id
      skills {
        id
        name
        shortName
        displayAtOverview
      }
    }
  }
`;

const NEW_SKILL = gql`
    mutation newSkill($organisationId: ID, $name: String, $shortName: String, $displayAtOverview: Boolean) {
     newSkill(organisationId: $organisationId, name:  $name, shortName: $shortName, displayAtOverview: $displayAtOverview) {
      id
    }
  }
`;

const EDIT_SKILL = gql`
    mutation editSkill($id: ID, $name: String, $shortName: String, $displayAtOverview: Boolean) {
     editSkill(id: $id, name:  $name, shortName: $shortName, displayAtOverview: $displayAtOverview) {
      id
    }
  }
`;

const DELETE_SKILL = gql`
    mutation deleteSkill($id: ID) {
     deleteSkill(id: $id) 
  }
`;

class SkillsEditMutation extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Query fetchPolicy="no-cache" query={SKILLS_BY_ORGANISATION_ID}
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
                <Mutation mutation={NEW_SKILL}
                          onCompleted={() => {
                            this.props.onSkillsChanged
                            && this.props.onSkillsChanged();
                            refetch();
                          }}>
                  { createNewSkill => (

                      <Mutation mutation={EDIT_SKILL}
                                onCompleted={() => {
                                  this.props.onSkillsChanged
                                  && this.props.onSkillsChanged();
                                  refetch();
                                }}>
                        { editSkill => (

                            <Mutation mutation={DELETE_SKILL}
                                      onCompleted={() => {
                                        this.props.onSkillsChanged
                                        && this.props.onSkillsChanged();
                                        refetch();
                                      }}>
                              { deleteSkill => (

                                  <SkillsEdit
                                      skills={data.organisationById.skills}

                                      onNewSkill={newSkill => {
                                        createNewSkill({
                                          variables: {
                                            organisationId: this.props.id,
                                            name: newSkill.name,
                                            shortName: newSkill.shortName,
                                            displayAtOverview: newSkill.displayAtOverview
                                          }
                                        });
                                      }}

                                      onSkillEdited={editedSkill => {
                                        editSkill({
                                          variables: {
                                            id: editedSkill.id,
                                            name: editedSkill.name,
                                            shortName: editedSkill.shortName,
                                            displayAtOverview: editedSkill.displayAtOverview
                                          }
                                        });
                                      }}

                                      onSkillDeleted={deletedSkill => {
                                        deleteSkill({
                                          variables: {
                                            id: deletedSkill.id
                                          }
                                        });
                                      }}
                                  />
                              )}
                            </Mutation>
                        )}
                      </Mutation>
                  )}
                </Mutation>
            );
          }}
        </Query>);
  }
}

export default SkillsEditMutation;