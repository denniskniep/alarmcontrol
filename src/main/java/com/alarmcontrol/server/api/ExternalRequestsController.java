package com.alarmcontrol.server.api;


import com.alarmcontrol.server.api.tetrapager.TetraPagerAlertParser;
import com.alarmcontrol.server.api.tetrapager.TetraPagerEmployeeFeedbackParser;
import com.alarmcontrol.server.api.tetrapager.TetraPagerEmployeeStatusParser;
import com.alarmcontrol.server.data.AlertService;
import com.alarmcontrol.server.data.EmployeeFeedbackService;
import com.alarmcontrol.server.data.EmployeeStatusService;
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
  private TetraPagerEmployeeStatusParser tetraPagerEmployeeStatusParser;
  private AlertService alertService;
  private EmployeeFeedbackService employeeFeedbackService;
  private EmployeeStatusService employeeStatusService;

  public ExternalRequestsController(TetraPagerAlertParser tetraPagerAlertParser,
      TetraPagerEmployeeFeedbackParser tetraPagerEmployeeFeedbackParser,
      TetraPagerEmployeeStatusParser tetraPagerEmployeeStatusParser,
      AlertService alertService, EmployeeFeedbackService employeeFeedbackService,
      EmployeeStatusService employeeStatusService) {
    this.tetraPagerAlertParser = tetraPagerAlertParser;
    this.tetraPagerEmployeeFeedbackParser = tetraPagerEmployeeFeedbackParser;
    this.tetraPagerEmployeeStatusParser = tetraPagerEmployeeStatusParser;
    this.alertService = alertService;
    this.employeeFeedbackService = employeeFeedbackService;
    this.employeeStatusService = employeeStatusService;
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

    if(alertCall == null){
      return ResponseEntity.ok("No AlertCall created!");
    }

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

  @ResponseBody
  @PostMapping(value = "/api/employeeStatus")
  public ResponseEntity<Object> addEmployeeStatus(@RequestBody HashMap<String, String> parameters) {
    Long organisationId = Parameter.getRequired(parameters, ORG_ID).asLong();
    ExternalEmployeeStatusRequest employeeStatusRequest = parseEmployeeStatusRequest(parameters);

    employeeStatusService.addEmployeeStatus(organisationId,
        employeeStatusRequest.getEmployeeReferenceId(),
        employeeStatusRequest.getStatus(),
        employeeStatusRequest.getDateTime(),
        employeeStatusRequest.getRaw());

    return ResponseEntity.ok().build();
  }

  private ExternalEmployeeStatusRequest parseEmployeeStatusRequest(HashMap<String, String> parameters) {
    return tetraPagerEmployeeStatusParser.parse(parameters);
  }
}
