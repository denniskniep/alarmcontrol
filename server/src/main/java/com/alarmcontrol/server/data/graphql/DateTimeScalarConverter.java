package com.alarmcontrol.server.data.graphql;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class DateTimeScalarConverter  implements Coercing<Date, String> {
  public static final CopyOnWriteArrayList<DateTimeFormatter> DATE_FORMATTERS = new CopyOnWriteArrayList<>();

  static {
    DATE_FORMATTERS.add(DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC));
    DATE_FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneOffset.UTC));
    DATE_FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneOffset.UTC));
  }

  private Date convertImpl(Object input) {
    if (input instanceof String) {
      LocalDateTime localDateTime = parseDate((String) input);

      if (localDateTime != null) {
        return toDate(localDateTime);
      }
    }
    return null;
  }

  public static Date toDate(LocalDateTime dateTime) {
    return Date.from(dateTime.atZone(ZoneOffset.UTC).toInstant());
  }

  @Override
  public String serialize(Object input) {
    if (input instanceof Date) {
      return toISOString((Date) input);
    } else {
      Date result = convertImpl(input);
      if (result == null) {
        throw new CoercingSerializeException("Invalid value '" + input + "' for Date");
      }
      return toISOString(result);
    }
  }

  @Override
  public Date parseValue(Object input) {
    Date result = convertImpl(input);
    if (result == null) {
      throw new CoercingParseValueException("Invalid value '" + input + "' for Date");
    }
    return result;
  }

  @Override
  public Date parseLiteral(Object input) {
    if (!(input instanceof StringValue)) return null;
    String value = ((StringValue) input).getValue();
    Date result = convertImpl(value);
    return result;
  }

  public static String toISOString(Date date) {
    return toISOString(toLocalDateTime(date));
  }

  public static LocalDateTime toLocalDateTime(Date date) {
    return date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
  }

  public static String toISOString(LocalDateTime dateTime) {
    return DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.of(dateTime, ZoneOffset.UTC));
  }

  public static LocalDateTime parseDate(String date) {
    for (DateTimeFormatter formatter : DATE_FORMATTERS) {
      try {
        // equals ISO_LOCAL_DATE
        if (formatter.equals(DATE_FORMATTERS.get(2))) {
          LocalDate localDate = LocalDate.parse(date, formatter);

          return localDate.atStartOfDay();
        } else {
          return LocalDateTime.parse(date, formatter);
        }
      } catch (java.time.format.DateTimeParseException ignored) {
      }
    }

    return null;
  }
}
