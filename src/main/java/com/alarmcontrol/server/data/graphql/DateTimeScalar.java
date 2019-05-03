package com.alarmcontrol.server.data.graphql;

import graphql.schema.GraphQLScalarType;
import org.springframework.stereotype.Component;

@Component
public class DateTimeScalar extends GraphQLScalarType {

  private static final String DEFAULT_NAME = "DateTime";

  public DateTimeScalar() {
    super(DEFAULT_NAME, "Date type", new DateTimeScalarConverter());
  }
}
