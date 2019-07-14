package com.alarmcontrol.server.data.graphql;


import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class GraphQLLocalTime extends GraphQLScalarType {

    private static final String DEFAULT_NAME = "LocalTime";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME.withZone(ZoneOffset.UTC);

    public GraphQLLocalTime() {
        this(DEFAULT_NAME);
    }

    public GraphQLLocalTime(final String name) {
        super(name, "Local Time type", new Coercing<LocalTime, String>() {
            private LocalTime convertImpl(Object input) {
                if (input instanceof String) {
                    try {
                        return LocalTime.parse((String) input, FORMATTER);
                    } catch (DateTimeParseException ignored) {
                    }
                }
                return null;
            }

            @Override
            public String serialize(Object input) {
                if (input instanceof LocalTime) {
                    return DateTimeHelper.toISOString((LocalTime) input);
                } else {
                    LocalTime result = convertImpl(input);
                    if (result == null) {
                        throw new CoercingSerializeException("Invalid value '" + input + "' for LocalTime");
                    }
                    return DateTimeHelper.toISOString(result);
                }
            }

            @Override
            public LocalTime parseValue(Object input) {
                LocalTime result = convertImpl(input);
                if (result == null) {
                    throw new CoercingParseValueException("Invalid value '" + input + "' for LocalTime");
                }
                return result;
            }

            @Override
            public LocalTime parseLiteral(Object input) {
                if (!(input instanceof StringValue)) return null;
                String value = ((StringValue) input).getValue();
                LocalTime result = convertImpl(value);
                return result;
            }
        });
    }

}
