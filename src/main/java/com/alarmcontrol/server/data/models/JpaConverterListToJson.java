package com.alarmcontrol.server.data.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class JpaConverterListToJson implements AttributeConverter<StringList, String> {

  private final static ObjectMapper objectMapper = new ObjectMapper();
  private Logger logger = LoggerFactory.getLogger(JpaConverterListToJson.class);

  @Override
  public String convertToDatabaseColumn(StringList meta) {
    try {
      return objectMapper.writeValueAsString(meta);
    } catch (Exception ex) {
      logger.error("Error during jpa data serialisation",ex);
      return null;
      // or throw an error
    }
  }

  @Override
  public StringList convertToEntityAttribute(String dbData) {
    if (dbData == null) return null;
    try {

      return objectMapper.readValue(dbData, StringList.class);
    } catch (Exception ex) {
      logger.error("Unexpected error during deserialisation json from database: " + dbData, ex);
      return null;
    }
  }

}

