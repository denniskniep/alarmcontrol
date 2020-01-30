package com.alarmcontrol.server.api.tetrapager;

import com.alarmcontrol.server.api.ExternalEmployeeFeedbackRequest;
import com.alarmcontrol.server.api.Parameter;
import com.alarmcontrol.server.data.models.Feedback;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TetraPagerEmployeeFeedbackParser {

  public static final String ISSI = "issi";
  public static final String UR = "ur";
  public static final String ID = "id";

  private static final String UR_ACK = "32768";
  private static final String UR_DENY = "32769";
  private static final String UR_WAIT = "32770";
  private static final String UR_TEST = "32771";

  private Logger logger = LoggerFactory.getLogger(TetraPagerEmployeeFeedbackParser.class);

  public ExternalEmployeeFeedbackRequest parse(Map<String, String> parameters) {
    Date dateTime = new Date();
    Parameter issi = Parameter.getRequired(parameters, ISSI).expectNumber();
    Parameter ur = Parameter.getRequired(parameters, UR).expectNumber();
    Parameter id = Parameter.getRequired(parameters, ID).expectNumber();

    String raw = Parameter.asString(issi, ur, id);
    Feedback feedback = convertUserResponseToFeedback(ur.getValue());
    logger.info("Associate UR '{}' with Feedback '{}'", ur.getValue(), feedback);

    return new ExternalEmployeeFeedbackRequest(raw,
        id.getValue(),
        issi.getValue(),
        feedback,
        dateTime);
  }

  private Feedback convertUserResponseToFeedback(String value) {
    switch (value) {
      case UR_ACK:
        return Feedback.COMMIT;
      case UR_DENY:
        return Feedback.CANCEL;
      case UR_WAIT:
        return Feedback.LATER;
      default:
        throw new IllegalArgumentException("The value '" + value + "' can not be converted to a Feedback!");
    }
  }
}
