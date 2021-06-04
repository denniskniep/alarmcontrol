package com.alarmcontrol.server.api.tetrapager;

import com.alarmcontrol.server.api.ExternalAlertRequest;
import com.alarmcontrol.server.api.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TetraPagerAlertParserV2 {

  public static final String GSSI = "gssi";
  public static final String ID = "id";
  public static final String TEXT = "text";
  public static final String WHITESPACE_REPLACEMENT_CHAR = "whitespaceReplacementChar";

  private static final Pattern SUBADDRESS_PATTERN = Pattern.compile("S\\d{2}");

  private Logger logger = LoggerFactory.getLogger(TetraPagerAlertParserV2.class);

  public ExternalAlertRequest parse(Map<String, String> parameters) {
    Date dateTime = new Date();
    Parameter gssi = Parameter.getRequired(parameters, GSSI).expectNumber();
    Parameter id = Parameter.getRequired(parameters, ID).expectNumber();
    Parameter text = Parameter.getRequired(parameters, TEXT);
    Parameter whitespaceReplacementChar = Parameter.getOptional(parameters, WHITESPACE_REPLACEMENT_CHAR);

    if(whitespaceReplacementChar != null){
      logger.info("Should replace '"+ whitespaceReplacementChar.getValue()+ "' with whitespace");
      text = new Parameter(text.getKey(), text.getValue().replace(whitespaceReplacementChar.getValue(), " "));
    }

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

    int startIndex = 0;
    for (String textPart : textParts) {
      String subaddress = getTextByRegExp(textPart, SUBADDRESS_PATTERN);
      if(!StringUtils.isBlank(subaddress)){
        logger.info("Found Subaddress in Textpart: {}", startIndex);
        break;
      }
      startIndex++;
    }

    if (textParts.length < startIndex + 1) {
      throw new IllegalArgumentException("Expected at least one part after part with index " + startIndex
          + " (separated by '*') for parameter '" + param.getKey() + "'. "
          + "But was '" + param.getValue() + "'");
    }

    String subaddress = extractSubaddress(textParts[startIndex]);
    logger.info("Subaddress:{}", subaddress);

    String keyword = tryGet(textParts, startIndex+1);
    logger.info("Keyword:{}", keyword);

    String description = tryGet(textParts, startIndex+2);
    logger.info("Description:{}", description);

    String address = tryGet(textParts, startIndex+3);
    logger.info("Address:{}", address);

    String alertId = keyword + "_" + (address == null ? "" : StringUtils.replace(address, " ", "-"));
    if(StringUtils.isBlank(keyword) && StringUtils.isBlank(address)){
      alertId = UUID.randomUUID().toString();
    }

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
