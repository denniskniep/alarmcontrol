import {Mutation, Query} from "react-apollo";
import React, {Component} from 'react';
import {gql} from "apollo-boost";
import EmployeesEdit from "./employeesEdit";

const EMPLOYEES_BY_ORGANISATION_ID = gql`
  query organisationById($id: ID) {
    organisationById(organisationId: $id) {
      id
      employees {
        id
        firstname
        lastname
        skills {
          id
          name
          shortName
        }
      }
      skills {
        id
        name
        shortName
        displayAtOverview
      }
    }
  }
`;

const NEW_EMPLOYEE = gql`
    mutation newEmployee($organisationId: ID, $firstname: String, $lastname: String) {
     newEmployee(organisationId: $organisationId, firstname:  $firstname, lastname: $lastname) {
      id
    }
  }
`;

const EDIT_EMPLOYEE = gql`
    mutation editEmployee($id: ID, $firstname: String, $lastname: String) {
     editEmployee(id: $id, firstname:  $firstname, lastname: $lastname) {
      id
    }
  }
`;

const DELETE_EMPLOYEE = gql`
    mutation deleteEmployee($id: ID) {
     deleteEmployee(id: $id) 
  }
`;

class EmployeesEditMutation extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Query fetchPolicy="no-cache" query={EMPLOYEES_BY_ORGANISATION_ID}
               variables={{id: this.props.id, shouldRefetch: this.props.refetch}}>
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
                <Mutation mutation={NEW_EMPLOYEE}
                          onCompleted={() => refetch()}>
                  {createNewEmployee => (

                      <Mutation mutation={EDIT_EMPLOYEE}
                                onCompleted={() => refetch()}>
                        {editEmployee => (

                            <Mutation mutation={DELETE_EMPLOYEE}
                                      onCompleted={() => refetch()}>
                              {deleteEmployee => (

                                  <EmployeesEdit
                                      employees={data.organisationById.employees}

                                      skills={data.organisationById.skills}

                                      onNewEmployee={newEmployee => {
                                        createNewEmployee({
                                          variables: {
                                            organisationId: this.props.id,
                                            firstname: newEmployee.firstname,
                                            lastname: newEmployee.lastname
                                          }
                                        });
                                      }}

                                      onEmployeeEdited={editedEmployee => {
                                        editEmployee({
                                          variables: {
                                            id: editedEmployee.id,
                                            firstname: editedEmployee.firstname,
                                            lastname: editedEmployee.lastname
                                          }
                                        });
                                      }}

                                      onEmployeeDeleted={deletedEmployee => {
                                        deleteEmployee({
                                          variables: {
                                            id: deletedEmployee.id
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

export default EmployeesEditMutation;