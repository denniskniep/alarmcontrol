package com.alarmcontrol.server.data.graphql;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.servlet.DefaultGraphQLErrorHandler;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CustomGraphQLErrorHandler extends DefaultGraphQLErrorHandler {

  @Override
  public List<GraphQLError> processErrors(List<GraphQLError> list) {
    return super.processErrors(list).stream().map(error -> {
      if(containsValidationException(error)){
        ClientValidationException validationException = castToValidationException(error);
        return new CustomGraphQLError(validationException.getMessage());
      }
      return error;
    }).collect(Collectors.toList());
  }

  private boolean containsValidationException(GraphQLError error) {
    return error instanceof ExceptionWhileDataFetching &&
        ((ExceptionWhileDataFetching) error).getException() instanceof ClientValidationException;
  }

  private ClientValidationException castToValidationException(GraphQLError error) {
    ExceptionWhileDataFetching exceptionWhileDataFetching = (ExceptionWhileDataFetching) error;
    return (ClientValidationException) exceptionWhileDataFetching.getException();
  }

}