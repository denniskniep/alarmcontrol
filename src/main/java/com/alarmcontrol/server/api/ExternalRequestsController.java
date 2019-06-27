package com.alarmcontrol.server;


import com.alarmcontrol.server.api.ExternalAlertRequest;
import com.alarmcontrol.server.api.ExternalEmployeeFeedbackRequest;
import com.alarmcontrol.server.api.Parameter;
import com.alarmcontrol.server.api.tetrapager.TetraPagerAlertParser;
import com.alarmcontrol.server.api.tetrapager.TetraPagerEmployeeFeedbackParser;
import com.alarmcontrol.server.data.AlertService;
import com.alarmcontrol.server.data.EmployeeFeedbackService;
import com.alarmcontrol.server.data.models.AlertCall;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExternalRequestsController {

  private static final String ORG_ID =  "organisationId";
  private TetraPagerAlertParser tetraPagerAlertParser;
  private TetraPagerEmployeeFeedbackParser tetraPagerEmployeeFeedbackParser;
  private AlertService alertService;
  private EmployeeFeedbackService employeeFeedbackService;

  public ExternalRequestsController(TetraPagerAlertParser tetraPagerAlertParser,
      TetraPagerEmployeeFeedbackParser tetraPagerEmployeeFeedbackParser,
      AlertService alertService, EmployeeFeedbackService employeeFeedbackService) {
    this.tetraPagerAlertParser = tetraPagerAlertParser;
    this.tetraPagerEmployeeFeedbackParser = tetraPagerEmployeeFeedbackParser;
    this.alertService = alertService;
    this.employeeFeedbackService = employeeFeedbackService;
  }

  @ResponseBody
  @PostMapping(value = "/api/alert")
  public ResponseEntity<Object> createAlert(@RequestBody HashMap<String, String> parameters) {
    Long organisationId = Parameter.getRequired(parameters, ORG_ID).asLong();
    ExternalAlertRequest alertRequest = parseAlertRequest(parameters);

    AlertCall alertCall = alertService.create(organisationId,
        alertRequest.getAlertNumber(),
        alertRequest.getAlertReferenceId(),
        alertRequest.getAlertCallReferenceId(),
        alertRequest.getKeyword(),
        alertRequest.getDateTime(),
        alertRequest.getAddress(),
        alertRequest.getDescription(),
        alertRequest.getRaw());

    return ResponseEntity.ok(alertCall.getId());
  }

  private ExternalAlertRequest parseAlertRequest(HashMap<String, String> parameters) {
    return tetraPagerAlertParser.parse(parameters);
  }

  @ResponseBody
  @PostMapping(value = "/api/employeeFeedback")
  public ResponseEntity<Object> addEmployeeFeedback(@RequestBody HashMap<String, String> parameters) {
    Long organisationId = Parameter.getRequired(parameters, ORG_ID).asLong();
    ExternalEmployeeFeedbackRequest employeeFeedbackRequest = parseEmployeeFeedbackRequest(parameters);

    employeeFeedbackService.addEmployeeFeedback(organisationId,
        employeeFeedbackRequest.getAlertCallReferenceId(),
        employeeFeedbackRequest.getEmployeeReferenceId(),
        employeeFeedbackRequest.getFeedback(),
        employeeFeedbackRequest.getDateTime(),
        employeeFeedbackRequest.getRaw());

    return ResponseEntity.ok().build();
  }

  private ExternalEmployeeFeedbackRequest parseEmployeeFeedbackRequest(HashMap<String, String> parameters) {
    return tetraPagerEmployeeFeedbackParser.parse(parameters);
  }
}
