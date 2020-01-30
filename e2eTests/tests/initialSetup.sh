#!/bin/bash

# Example Execution
# URL="http://localhost:8080/graphql"  ./initialSetup.sh

# Exit immediately if a command exits with a non-zero status.
set -e

# Treat unset variables as an error when substituting.
set -u

function executeGQLQuery(){
  local gql_body="$1"
  #http://alarmcontrol:8080/graphql
  curl -v -X POST "${URL}" \
  -H 'Content-Type: application/json' \
  --data-binary "${gql_body}"
}

# Alert Numbers
executeGQLQuery '{"query":"mutation { \r\n  newOrganisation(\r\n    name: \"FF Meimbressen\"\r\n    addressLat: \"51.406339\"\r\n    addressLng: \"9.359186\"\r\n   location: \"Calden-Meimbressen\"\r\n  ){id}\r\n}"}'

executeGQLQuery '{"query":"mutation { \r\n  newAlertNumber (\r\n    organisationId: 1\r\n    number: \"123456-S04\"\r\n    shortDescription: \"Pager\"\r\n description: \"Pager Meimbressen\"\r\n  ){id}\r\n}"}'

executeGQLQuery '{"query":"mutation { \r\n  newAlertNumber (\r\n    organisationId: 1\r\n    number: \"123456-S54\"\r\n    shortDescription: \"Sirene\"\r\n    description: \"Sirene Meimbressen\"\r\n  ){id}\r\n}"}'

# Skills
executeGQLQuery '{"query":"mutation { \r\n  newSkill (\r\n    organisationId: 1\r\n    name: \"Atemschutzger\u00E4tetr\u00E4ger\"\r\n    shortName: \"AGT\"\r\n    displayAtOverview: true    \r\n  ){id}\r\n}"}'

executeGQLQuery '{"query":"mutation { \r\n  newSkill (\r\n    organisationId: 1\r\n    name: \"F\u00FChrungskraft\"\r\n    shortName: \"FK\"\r\n    displayAtOverview: true    \r\n  ){id}\r\n}"}'

executeGQLQuery '{"query":"mutation { \r\n  newSkill (\r\n    organisationId: 1\r\n    name: \"Min. C1 (Fahrer > 3,5t)\"\r\n    shortName: \"C1\"\r\n    displayAtOverview: true    \r\n  ){id}\r\n}\r\n"}'

# Employees
executeGQLQuery '{"query":"mutation { \r\n  newEmployee(\r\n    organisationId: 1\r\n    firstname: \"Lars\"\r\n    lastname: \"Laune\"\r\n    referenceId: \"1234\"\r\n  ){id}\r\n}\r\n"}'

executeGQLQuery '{"query":"mutation { \r\n  newEmployee(\r\n    organisationId: 1\r\n    firstname: \"Malte\"\r\n    lastname: \"Malteser\"\r\n    referenceId: \"2345\"\r\n  ){id}\r\n}\r\n"}'

executeGQLQuery '{"query":"mutation { \r\n  newEmployee(\r\n    organisationId: 1\r\n    firstname: \"Erika\"\r\n    lastname: \"Mustermann\"\r\n    referenceId: \"3456\"\r\n  ){id}\r\n}\r\n"}'

executeGQLQuery '{"query":"mutation { \r\n  newEmployee(\r\n    organisationId: 1\r\n    firstname: \"Max\"\r\n    lastname: \"Mustermann\"\r\n    referenceId: \"4567\"\r\n  ){id}\r\n}"}'

# EmployeeSkill
executeGQLQuery '{"query":"mutation { \r\n  addEmployeeSkill(employeeId: 1, skillId: 1)\r\n}"}'

executeGQLQuery '{"query":"mutation { \r\n  addEmployeeSkill(employeeId:1, skillId: 2)\r\n}"}'

executeGQLQuery '{"query":"mutation { \r\n  addEmployeeSkill(employeeId:2, skillId: 1)\r\n}"}'


