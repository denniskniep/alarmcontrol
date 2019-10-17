package com.alarmcontrol.server;

import com.alarmcontrol.server.data.AlertService;
import com.alarmcontrol.server.data.TestConfiguration;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.alarmcontrol.server.data.utils.GraphQLClient;
import com.alarmcontrol.server.data.utils.TestOrganisation;
import com.alarmcontrol.server.notifications.core.NotificationService;
import java.util.UUID;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public abstract class AlertBaseTest {

  public static final String EMPLOYEE_HANS = "123";
  public static final String EMPLOYEE_MARINA = "124";
  public static final String EMPLOYEE_EDUARD = "125";
  public static final String EMPLOYEE_SABINE = "126";

  @Autowired
  protected AlertService alertService;

  @Autowired
  protected GraphQLClient graphQlClient;

  @Autowired
  protected AlertRepository alertRepository;

  @Autowired
  protected NotificationService notificationService;

  protected TestOrganisation setupOrganisation() {
    TestOrganisation org = graphQlClient.createOrganisation("Organisation" + UUID.randomUUID());
    org.addAlertNumber("1234-S04", "Pager");
    return org;
  }

  protected TestOrganisation setupOrganisationWithEmployees() {
    TestOrganisation organisation = setupOrganisation();
    Long idHans = organisation.addEmployee("Hans", "Meier", EMPLOYEE_HANS);
    Long idMarina = organisation.addEmployee("Marina", "Mustermann", EMPLOYEE_MARINA);
    Long idEduard = organisation.addEmployee("Eduard", "Lampe", EMPLOYEE_EDUARD);
    Long idSabine = organisation.addEmployee("Sabine", "Sonntag", EMPLOYEE_SABINE);

    Long skillAGT = organisation.addSkill("Atemschutz", "AGT", true);
    Long skillFK = organisation.addSkill("Führungskraft", "FK", true);

    organisation.addEmployeeSkill(idHans, skillAGT);
    organisation.addEmployeeSkill(idHans, skillFK);
    organisation.addEmployeeSkill(idMarina, skillAGT);
    organisation.addEmployeeSkill(idSabine, skillAGT);

    return organisation;
  }
}
