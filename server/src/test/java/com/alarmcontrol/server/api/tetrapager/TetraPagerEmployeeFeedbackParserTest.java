package com.alarmcontrol.server.api.tetrapager;


import static org.assertj.core.api.Assertions.assertThat;

import com.alarmcontrol.server.api.ExternalEmployeeFeedbackRequest;
import com.alarmcontrol.server.data.models.Feedback;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TetraPagerEmployeeFeedbackParserTest {

  @Test
  public void parse_commit(){
    ExternalEmployeeFeedbackRequest request = parseFeedback("32768");

    assertThat(request.getEmployeeReferenceId()).isEqualTo("1234567");
    assertThat(request.getAlertCallReferenceId()).isEqualTo("123");
    assertThat(request.getFeedback()).isEqualTo(Feedback.COMMIT);
  }

  @Test
  public void parse_cancel(){
    ExternalEmployeeFeedbackRequest request = parseFeedback("32769");

    assertThat(request.getEmployeeReferenceId()).isEqualTo("1234567");
    assertThat(request.getAlertCallReferenceId()).isEqualTo("123");
    assertThat(request.getFeedback()).isEqualTo(Feedback.CANCEL);
  }

  @Test
  public void parse_later(){
    ExternalEmployeeFeedbackRequest request = parseFeedback("32770");

    assertThat(request.getEmployeeReferenceId()).isEqualTo("1234567");
    assertThat(request.getAlertCallReferenceId()).isEqualTo("123");
    assertThat(request.getFeedback()).isEqualTo(Feedback.LATER);
  }

  private ExternalEmployeeFeedbackRequest parseFeedback(String userResponse) {
    TetraPagerEmployeeFeedbackParser tetraPagerEmployeeFeedbackParser = new TetraPagerEmployeeFeedbackParser();

    Map<String,String> params = new HashMap<>();
    params.put(TetraPagerEmployeeFeedbackParser.ISSI, "1234567");
    params.put(TetraPagerEmployeeFeedbackParser.ID, "123");
    params.put(TetraPagerEmployeeFeedbackParser.UR, userResponse);

    return tetraPagerEmployeeFeedbackParser.parse(params);
  }
}