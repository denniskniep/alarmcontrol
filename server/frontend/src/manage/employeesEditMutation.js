import React, {Component} from 'react';
import {gql} from "apollo-boost";
import EmployeesEdit from "./employeesEdit";
import QueryHandler from "../utils/queryHandler";
import MutationHandler from "../utils/mutationHandler";

const EMPLOYEES_BY_ORGANISATION_ID = gql`
  query organisationById($id: ID!) {
    organisationById(organisationId: $id) {
      id
      employees {
        id
        firstname
        lastname
        referenceId
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
    mutation newEmployee($organisationId: ID!, $firstname: String!, $lastname: String!, $referenceId: String!) {
     newEmployee(organisationId: $organisationId, firstname:  $firstname, lastname: $lastname, referenceId: $referenceId) {
      id
    }
  }
`;

const EDIT_EMPLOYEE = gql`
    mutation editEmployee($id: ID!, $firstname: String!, $lastname: String!, $referenceId: String!) {
     editEmployee(id: $id, firstname:  $firstname, lastname: $lastname, referenceId: $referenceId) {
      id
    }
  }
`;

const DELETE_EMPLOYEE = gql`
    mutation deleteEmployee($id: ID!) {
     deleteEmployee(id: $id) 
  }
`;

const ADD_EMPLOYEE_SKILL = gql`
  mutation addEmployeeSkill($employeeId: ID!, $skillId: ID!) {
     addEmployeeSkill(employeeId: $employeeId, skillId: $skillId)
  }
`;

const DELETE_EMPLOYEE_SKILL = gql`
  mutation deleteEmployeeSkill($employeeId: ID!, $skillId: ID!) {
     deleteEmployeeSkill(employeeId: $employeeId, skillId: $skillId)
  }
`;

class EmployeesEditMutation extends Component {

  constructor(props) {
    super(props);
  }

  handleNewEmployee(newEmployee, createNewEmployee, addEmployeeSkill) {
    createNewEmployee({
      variables: {
        organisationId: this.props.id,
        firstname: newEmployee.firstname,
        lastname: newEmployee.lastname,
        referenceId: newEmployee.referenceId
      }
    }).then((result) => {
      newEmployee.skills.forEach(
          (s) => {
            addEmployeeSkill({
              variables: {
                employeeId: result.data.newEmployee.id,
                skillId: s.id
              }
            });
          })
    });
  }

  handleEmployeeEdited(oldEmployee,
      newEmployee,
      editEmployee,
      addEmployeeSkill,
      deleteEmployeeSkill) {
    editEmployee({
      variables: {
        id: newEmployee.id,
        firstname: newEmployee.firstname,
        lastname: newEmployee.lastname,
        referenceId: newEmployee.referenceId
      }
    });

    const oldIds = oldEmployee.skills.map(s => s.id);
    const newIds = newEmployee.skills.map(s => s.id);

    const toDelete = oldIds.filter(id => !newIds.includes(id));
    const toAdd = newIds.filter(id => !oldIds.includes(id));

    toDelete.forEach(
        (id) => {
          deleteEmployeeSkill({
            variables: {
              employeeId: newEmployee.id,
              skillId: id
            }
          });
        });

    toAdd.forEach(
        (id) => {
          addEmployeeSkill({
            variables: {
              employeeId: newEmployee.id,
              skillId: id
            }
          });
        });
  }

  handleEmployeeDeleted(deletedEmployee, deleteEmployee) {
    deleteEmployee({
      variables: {
        id: deletedEmployee.id
      }
    });
  }

  render() {
    return (
        <QueryHandler  fetchPolicy="no-cache"
                       query={EMPLOYEES_BY_ORGANISATION_ID}
                       variables={{
                         id: this.props.id,
                         shouldRefetch: this.props.refetch
                       }}>
          {({data, refetch}) => {

            if (data && !data.organisationById) {
              return <React.Fragment></React.Fragment>;
            }

            return (
                <MutationHandler mutation={NEW_EMPLOYEE}
                                 onCompleted={() => refetch()}>
                  {createNewEmployee => (

                      <MutationHandler mutation={EDIT_EMPLOYEE}
                                       onCompleted={() => refetch()}>
                        {editEmployee => (

                            <MutationHandler mutation={DELETE_EMPLOYEE}
                                             onCompleted={() => refetch()}>
                              {deleteEmployee => (

                                  <MutationHandler mutation={ADD_EMPLOYEE_SKILL}
                                                   onCompleted={() => refetch()}>
                                    {addEmployeeSkill => (

                                        <MutationHandler mutation={DELETE_EMPLOYEE_SKILL}
                                                         onCompleted={() => refetch()}>
                                          {deleteEmployeeSkill => (

                                              <EmployeesEdit
                                                  employees={data.organisationById.employees}

                                                  skills={data.organisationById.skills}

                                                  onNewEmployee={newEmployee =>
                                                      this.handleNewEmployee(
                                                          newEmployee,
                                                          createNewEmployee,
                                                          addEmployeeSkill)}

                                                  onEmployeeEdited={
                                                    (oldEmployee, newEmployee )=>
                                                        this.handleEmployeeEdited(
                                                            oldEmployee,
                                                            newEmployee,
                                                            editEmployee,
                                                            addEmployeeSkill,
                                                            deleteEmployeeSkill
                                                        )}

                                                  onEmployeeDeleted={
                                                    deletedEmployee =>
                                                        this.handleEmployeeDeleted(
                                                            deletedEmployee,
                                                            deleteEmployee)
                                                  }
                                              />
                                          )}
                                        </MutationHandler>
                                    )}
                                  </MutationHandler>
                              )}
                            </MutationHandler>
                        )}
                      </MutationHandler>
                  )}
                </MutationHandler>
            );
          }}
        </QueryHandler>);
  }
}

export default EmployeesEditMutation;