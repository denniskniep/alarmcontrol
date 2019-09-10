package com.alarmcontrol.server.data.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.jayway.jsonpath.PathNotFoundException;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class GraphQLClient {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private TestRestTemplate restTemplate;

  @Value("${graphql.servlet.mapping:/graphql}")
  private String graphqlMapping;

  public GraphQLResponse perform(String graphql, ObjectNode variables) {
    String payload = createGraphQlQuery(graphql, variables);
    GraphQLResponse response = this.post(payload);

    try{
      JSONArray errors = response.context().read("errors");
      if(!errors.isEmpty() ){
        throw new RuntimeException("GraphQl Response contains errors\n"+ errors);
      }
    }catch (PathNotFoundException e){
      //No errors found
    }

    return response;
  }

  private String createGraphQlQuery(String graphql, ObjectNode variables) {
    ObjectNode wrapper = this.objectMapper.createObjectNode();
    wrapper.put("query", graphql);
    wrapper.set("variables", variables);

    try {
      return this.objectMapper.writeValueAsString(wrapper);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private GraphQLResponse post(String payload) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    return this.postRequest(new HttpEntity(payload, headers));
  }

  private GraphQLResponse postRequest(HttpEntity<Object> request) {
    ResponseEntity<String> response = this.restTemplate
        .exchange(this.graphqlMapping, HttpMethod.POST, request, String.class, new Object[0]);
    return new GraphQLResponse(response);
  }

  public TestOrganisation createOrganisation(String name) {
    GraphQLResponse response = perform(""
            + "mutation newOrganisation($name: String!, $addressLat: String!, $addressLng: String!) {\n"
            + "  newOrganisation(name: $name, addressLat: $addressLat, addressLng: $addressLng) {\n"
            + "    id\n"
            + "  }\n"
            + "}",
        Vars.create()
            .put("name", name)
            .put("addressLat", TestOrganisation.ORG_ADDRESS_LAT)
            .put("addressLng", TestOrganisation.ORG_ADDRESS_LNG)
    );

    Long orgId = Long.valueOf(response.get("$.data.newOrganisation.id"));
    return new TestOrganisation(this, orgId);
  }
}
