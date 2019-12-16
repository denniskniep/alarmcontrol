package com.alarmcontrol.server.api.tetrapager;

import com.alarmcontrol.server.api.ExternalEmployeeStatusRequest;
import com.alarmcontrol.server.api.Parameter;
import com.alarmcontrol.server.data.models.Status;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TetraPagerEmployeeStatusParser {

  public static final String ISSI = "issi";
  public static final String STATUS = "status";

  private static final String AVAILABLE = "15";
  private static final String NOT_AVAILABLE = "0";

  private Logger logger = LoggerFactory.getLogger(TetraPagerEmployeeStatusParser.class);

  public ExternalEmployeeStatusRequest parse(Map<String, String> parameters) {
    Date dateTime = new Date();
    Parameter issi = Parameter.getRequired(parameters, ISSI).expectNumber();
    Parameter status = Parameter.getRequired(parameters, STATUS).expectNumber();

    String raw = Parameter.asString(issi, status);
    Status parsedStatus = convertValueToStatus(status.getValue());
    logger.info("Associate Value '{}' with Status '{}'", parsedStatus.getValue(), parsedStatus);

    return new ExternalEmployeeStatusRequest(raw,
        issi.getValue(),
        parsedStatus,
        dateTime);
  }

  private Status convertValueToStatus(String value) {
    switch (value) {
      case AVAILABLE:
        return Status.AVAILABLE;
      case NOT_AVAILABLE:
        return Status.NOT_AVAILABLE;
      default:
        throw new IllegalArgumentException("The value '" + value + "' can not be converted to a Status!");
    }
  }
}
