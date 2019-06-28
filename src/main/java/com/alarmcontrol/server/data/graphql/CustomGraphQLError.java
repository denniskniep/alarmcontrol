package com.alarmcontrol.server.data.graphql;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import java.util.List;

public class CustomGraphQLError implements GraphQLError {

  private String message;

  public CustomGraphQLError(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public List<SourceLocation> getLocations() {
    return null;
  }

  @Override
  public ErrorType getErrorType() {
    return null;
  }
}
