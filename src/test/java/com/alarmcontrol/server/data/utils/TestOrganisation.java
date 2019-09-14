package com.alarmcontrol.server.data.utils;

import com.graphql.spring.boot.test.GraphQLResponse;

public class TestOrganisation {

  public static final String ORG_ADDRESS_LAT = "51.406339";
  public static final String ORG_ADDRESS_LNG = "9.359186";

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
}
