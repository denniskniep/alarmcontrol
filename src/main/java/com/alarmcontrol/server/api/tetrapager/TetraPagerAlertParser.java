package com.alarmcontrol.server.api.tetrapager;

import com.alarmcontrol.server.api.ExternalAlertRequest;
import com.alarmcontrol.server.api.Parameter;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TetraPagerAlertParser {

  public static final String GSSI = "gssi";
  public static final String ID = "id";
  public static final String TEXT = "text";

  private static final Pattern ALERT_ID_PATTERN = Pattern.compile("B\\d{4,}");
  private static final Pattern SUBADDRESS_PATTERN = Pattern.compile("S\\d{2}");

  private Logger logger = LoggerFactory.getLogger(TetraPagerAlertParser.class);

  public ExternalAlertRequest parse(Map<String, String> parameters) {
    Date dateTime = new Date();
    Parameter gssi = Parameter.getRequired(parameters, GSSI).expectNumber();
    Parameter id = Parameter.getRequired(parameters, ID).expectNumber();
    Parameter text = Parameter.getRequired(parameters, TEXT);

    String raw = Parameter.asString(gssi, id, text);
    ParsedText parsedText = parseText(text);

    String alertNumber = gssi.getValue() + "-" +  parsedText.getSubaddress();
    String alertReferenceId = parsedText.getAlertId() != null ? parsedText.getAlertId() : id.getValue();
    String alertCallReferenceId = id.getValue();

    return new ExternalAlertRequest(raw,
        alertNumber,
        alertReferenceId,
        alertCallReferenceId,
        parsedText.getKeyword(),
        parsedText.getDescription(),
        parsedText.getAddress(),
        dateTime);
  }

  private ParsedText parseText(Parameter param) {
    String text = param.getValue();

    String[] textParts = text.split("\\*");
    if (textParts.length < 2) {
      throw new IllegalArgumentException("Expected at least two parts "
          + "(separated by '*') for parameter '" + param.getKey() + "'. "
          + "But was '" + param.getValue() + "'");
    }

    String subaddress = extractSubaddress(textParts[0]);
    logger.info("Subaddress:{}", subaddress);
    String description = textParts[1];

    String alertId = getTextByRegExp(description, ALERT_ID_PATTERN);
    logger.info("AlertId:{}",alertId);

    if(!StringUtils.isBlank(alertId)){
      logger.debug("There is an alertId in the description. Remove alertId from description!");
      description = description.replace(alertId, "").trim();
      if(StringUtils.isBlank(description)){
        logger.debug("No description left, setting to null");
        description = null;
      }
    }
    logger.info("Description:{}", description);

    String keyword = tryGet(textParts, 2);
    if(StringUtils.isBlank(keyword) && StringUtils.equalsIgnoreCase(description, "FUNKTIONSPROBE")){
      logger.debug("keyword is blank and description is 'FUNKTIONSPROBE'. Switch values of keyword and description");
      keyword = description;
      description = null;
    }
    logger.info("Keyword:{}", keyword);

    String address = tryGet(textParts, 3);
    logger.info("Address:{}", address);

    return new ParsedText(subaddress,
        keyword,
        description,
        address,
        alertId);
  }

  private String tryGet(String[] textParts, int index){
    if (textParts.length > index) {
      return textParts[index];
    }
    return null;
  }

  private String extractSubaddress(String textPart) {
    String subaddress = getTextByRegExp(textPart, SUBADDRESS_PATTERN);
    if (StringUtils.isBlank(subaddress)) {
      throw new IllegalArgumentException("The parameter 'text' does not contain a subaddress at the expected position."
          + "Tried to parse '"+textPart+"'");
    }

    return subaddress;
  }

  private String getTextByRegExp(String value, Pattern regExp) {
    logger.debug("Try to find pattern '{}' in value '{}'", regExp.pattern(), value);
    Matcher matcher = regExp.matcher(value);
    while (matcher.find()) {
      String found = matcher.group();
      logger.debug("Found '{}'", found);
      return found;
    }
    return null;
  }

  private static class ParsedText {

    private String subaddress;
    private String keyword;
    private String description;
    private String address;
    private String alertId;

    public ParsedText(String subaddress, String keyword, String description, String address, String alertId) {
      this.subaddress = subaddress;
      this.keyword = keyword;
      this.description = description;
      this.address = address;
      this.alertId = alertId;
    }

    public String getSubaddress() {
      return subaddress;
    }

    public String getKeyword() {
      return keyword;
    }

    public String getDescription() {
      return description;
    }

    public String getAddress() {
      return address;
    }

    public String getAlertId() {
      return alertId;
    }
  }
}
