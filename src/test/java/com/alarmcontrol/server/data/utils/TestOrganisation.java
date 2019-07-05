package com.alarmcontrol.server.data.utils;

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
}
