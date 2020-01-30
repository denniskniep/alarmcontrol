package com.alarmcontrol.server.data.utils;

import com.alarmcontrol.server.data.models.Feedback;
import com.graphql.spring.boot.test.GraphQLResponse;

public class TestOrganisation {

  public static final String ORG_ADDRESS_LAT = "51.406339";
  public static final String ORG_ADDRESS_LNG = "9.359186";
  public static final String ORG_LOCATION = "Calden-Meimbressen";

  private GraphQLClient graphQLClient;
  private Long organisationId;

  public TestOrganisation(GraphQLClient graphQLClient, Long organisationId) {
    this.graphQLClient = graphQLClient;
    this.organisationId = organisationId;
  }

  public Long getId(){
    return organisationId;
  }

  public void addAlertNumber(String number, String shortDescription){
    graphQLClient.perform(""+
            "mutation newAlertNumber($organisationId: ID!, $number: String!, $description: String, $shortDescription: String!) { " +
            "    newAlertNumber(organisationId: $organisationId, number: $number, description: $description, shortDescription: $shortDescription) { "+
            "      id"+
            "    }"+
            "}",
        Vars.create()
            .put("organisationId", organisationId)
            .put("number", number)
            .put("description", "DOES_NOT_MATTER")
            .put("shortDescription", shortDescription)
    );
  }

  public Long addEmployee(String firstname, String lastname, String referenceId){
    GraphQLResponse response = graphQLClient.perform("" +
        "mutation newEmployee($organisationId: ID!, $firstname: String!, $lastname: String!, $referenceId: String!) { " +
        "    newEmployee(organisationId: $organisationId, firstname: $firstname, lastname: $lastname, referenceId: $referenceId) {"+
        "      id"+
        "    }"+
        "}",
        Vars.create()
            .put("organisationId", organisationId)
            .put("firstname", firstname)
            .put("lastname", lastname)
            .put("referenceId", referenceId)
    );

    return Long.valueOf(response.get("data.newEmployee.id"));
  }

  public Long addSkill(String name, String shortName, Boolean displayAtOverview){
    GraphQLResponse response = graphQLClient.perform("" +
            "mutation newSkill($organisationId: ID!, $name: String!, $shortName: String!, $displayAtOverview: Boolean!) { " +
            "    newSkill(organisationId: $organisationId, name: $name, shortName: $shortName, displayAtOverview: $displayAtOverview) {"+
            "      id"+
            "    }"+
            "}",
        Vars.create()
            .put("organisationId", organisationId)
            .put("name", name)
            .put("shortName", shortName)
            .put("displayAtOverview", displayAtOverview)
    );

    return Long.valueOf(response.get("data.newSkill.id"));
  }

  public void addEmployeeFeedback(String alertCallReferenceId, String employeeReferenceId, Feedback feedback){
    GraphQLResponse response = graphQLClient.perform("" +
            "mutation addEmployeeFeedback($organisationId: ID!, $alertCallReferenceId: String!, $employeeReferenceId: String!, $feedback: Feedback!) { " +
            "    addEmployeeFeedback(organisationId: $organisationId, alertCallReferenceId: $alertCallReferenceId, employeeReferenceId: $employeeReferenceId, feedback: $feedback) { " +
              "feedback" +
              "} " +
            "}",
        Vars.create()
            .put("organisationId", organisationId)
            .put("alertCallReferenceId", alertCallReferenceId)
            .put("employeeReferenceId", employeeReferenceId)
            .put("feedback", feedback.toString())
    );
  }

  public void addEmployeeSkill(Long employeeId, Long skillId){
    GraphQLResponse response = graphQLClient.perform("" +
            "mutation addEmployeeSkill($employeeId: ID!, $skillId: ID!) { " +
            "    addEmployeeSkill(employeeId: $employeeId, skillId: $skillId) " +
            "}",
        Vars.create()
            .put("employeeId", employeeId)
            .put("skillId", skillId)
    );
  }
}
