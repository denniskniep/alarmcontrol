package com.alarmcontrol.server.data.graphql;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import java.util.List;

public class ClientValidationException extends RuntimeException implements GraphQLError {

  public ClientValidationException() {
  }

  public ClientValidationException(String message) {
    super(message);
  }

  public ClientValidationException(String message, Throwable cause) {
    super(message, cause);
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
