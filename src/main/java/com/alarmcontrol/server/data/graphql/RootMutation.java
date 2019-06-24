package com.alarmcontrol.server.data.graphql;

import com.alarmcontrol.server.data.AlertService;
import com.alarmcontrol.server.data.graphql.employeeFeedback.EmployeeFeedback;
import com.alarmcontrol.server.data.graphql.employeeFeedback.publisher.EmployeeFeedbackForAlertAddedPublisher;
import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.models.AlertCallEmployee;
import com.alarmcontrol.server.data.models.AlertNumber;
import com.alarmcontrol.server.data.models.Employee;
import com.alarmcontrol.server.data.models.EmployeeSkill;
import com.alarmcontrol.server.data.models.Feedback;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.models.Skill;
import com.alarmcontrol.server.data.repositories.AlertCallEmployeeRepository;
import com.alarmcontrol.server.data.repositories.AlertCallRepository;
import com.alarmcontrol.server.data.repositories.AlertNumberRepository;
import com.alarmcontrol.server.data.repositories.EmployeeRepository;
import com.alarmcontrol.server.data.repositories.EmployeeSkillRepository;
import com.alarmcontrol.server.data.repositories.OrganisationRepository;
import com.alarmcontrol.server.data.repositories.SkillRepository;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class RootMutation implements GraphQLMutationResolver {

  private AlertService alertService;
  private OrganisationRepository organisationRepository;
  private EmployeeRepository employeeRepository;
  private SkillRepository skillRepository;
  private EmployeeSkillRepository employeeSkillRepository;
  private EmployeeFeedbackForAlertAddedPublisher employeeFeedbackForAlertAddedPublisher;
  private AlertNumberRepository alertNumberRepository;
  private AlertCallRepository alertCallRepository;
  private AlertCallEmployeeRepository alertCallEmployeeRepository;

  public RootMutation(AlertService alertService,
      OrganisationRepository organisationRepository,
      EmployeeRepository employeeRepository,
      SkillRepository skillRepository,
      EmployeeSkillRepository employeeSkillRepository,
      EmployeeFeedbackForAlertAddedPublisher employeeFeedbackForAlertAddedPublisher,
      AlertNumberRepository alertNumberRepository,
      AlertCallRepository alertCallRepository,
      AlertCallEmployeeRepository alertCallEmployeeRepository) {
    this.alertService = alertService;
    this.organisationRepository = organisationRepository;
    this.employeeRepository = employeeRepository;
    this.skillRepository = skillRepository;
    this.employeeSkillRepository = employeeSkillRepository;
    this.employeeFeedbackForAlertAddedPublisher = employeeFeedbackForAlertAddedPublisher;
    this.alertNumberRepository = alertNumberRepository;
    this.alertCallRepository = alertCallRepository;
    this.alertCallEmployeeRepository = alertCallEmployeeRepository;
  }

  public AlertCall newAlertCall(Long organisationId,
      String alertNumber,
      String alertReferenceId,
      String alertCallReferenceId,
      String keyword,
      Date dateTime,
      String address) {
    return alertService
        .create(organisationId, alertNumber, alertReferenceId, alertCallReferenceId, keyword, dateTime, address);
  }


  public EmployeeFeedback addEmployeeFeedback(Long organisationId,
      String alertCallReferenceId,
      String employeeReferenceId,
      Feedback feedback) {

    Date dateTime = new Date();

    Optional<AlertCall> foundAlertCall = alertCallRepository
        .findByOrganisationIdAndReferenceId(organisationId, alertCallReferenceId);

    if (foundAlertCall.isEmpty()) {
      throw new IllegalArgumentException("No AlertCall found for referenceId '" + alertCallReferenceId + "'"
          + " in organisationId '" + organisationId + "'");
    }

    Optional<Employee> foundEmployee = employeeRepository
        .findByOrganisationIdAndReferenceId(organisationId, employeeReferenceId);

    if (foundEmployee.isEmpty()) {
      throw new IllegalArgumentException("No Employee found for referenceId '" + employeeReferenceId + "'"
          + " in organisationId '" + organisationId + "'");
    }

    AlertCallEmployee alertCallEmployee = new AlertCallEmployee(foundEmployee.get().getId(),
        foundAlertCall.get().getId(), feedback, "", dateTime);

    alertCallEmployeeRepository.save(alertCallEmployee);

    employeeFeedbackForAlertAddedPublisher
        .emitEmployeeFeedbackForAlertAdded(alertCallEmployee.getAlertCallId(), alertCallEmployee.getEmployeeId());

    return new EmployeeFeedback(alertCallEmployee.getEmployeeId(),
        alertCallEmployee.getFeedback(),
        alertCallEmployee.getDateTime());
  }

  public Employee newEmployee(Long organisationId,
      String firstname,
      String lastname,
      String referenceId) {
    Employee employee = new Employee(organisationId, firstname, lastname, referenceId);
    return employeeRepository.save(employee);
  }

  public Long deleteEmployee(Long id) {
    Optional<Employee> employeeById = employeeRepository.findById(id);
    if (!employeeById.isPresent()) {
      throw new RuntimeException("No Employee found for id:" + id);
    }

    List<EmployeeSkill> employeeSkills = employeeSkillRepository
        .findByEmployeeId(id);
    employeeSkillRepository.deleteAll(employeeSkills);

    Employee employee = employeeById.get();
    employeeRepository.delete(employee);
    return employee.getId();
  }

  public Employee editEmployee(Long id,
      String firstname,
      String lastname,
      String referenceId) {

    Optional<Employee> employeeById = employeeRepository.findById(id);
    if (!employeeById.isPresent()) {
      throw new RuntimeException("No Employee found for id:" + id);
    }

    Employee employee = employeeById.get();
    employee.setFirstname(firstname);
    employee.setLastname(lastname);
    employee.setReferenceId(referenceId);

    employeeRepository.save(employee);
    return employee;
  }

  public Organisation newOrganisation(String name, String addressLat, String addressLng) {
    Organisation org = new Organisation(name, addressLat, addressLng);
    organisationRepository.save(org);
    return org;
  }

  public Organisation editOrganisation(Long id, String name, String addressLat, String addressLng) {
    Optional<Organisation> organisationById = organisationRepository.findById(id);
    if (!organisationById.isPresent()) {
      throw new RuntimeException("No Organisation found for id:" + id);
    }

    Organisation organisation = organisationById.get();
    organisation.setName(name);
    organisation.setAddressLat(addressLat);
    organisation.setAddressLng(addressLng);
    organisationRepository.save(organisation);
    return organisation;
  }

  public Long deleteOrganisation(Long id) {
    Optional<Organisation> organisationById = organisationRepository.findById(id);
    if (!organisationById.isPresent()) {
      throw new RuntimeException("No Organisation found for id:" + id);
    }

    Organisation organisation = organisationById.get();
    organisationRepository.delete(organisation);
    return organisation.getId();
  }

  public Skill newSkill(Long organisationId, String name, String shortName, boolean displayAtOverview) {
    Skill skill = new Skill(organisationId, name, shortName, displayAtOverview);
    skillRepository.save(skill);
    return skill;
  }

  public Skill editSkill(Long id, String name, String shortName, boolean displayAtOverview) {
    Optional<Skill> skillById = skillRepository.findById(id);
    if (!skillById.isPresent()) {
      throw new RuntimeException("No Skill found for id:" + id);
    }

    Skill skill = skillById.get();
    skill.setName(name);
    skill.setShortName(shortName);
    skill.setDisplayAtOverview(displayAtOverview);

    skillRepository.save(skill);
    return skill;
  }

  public Long deleteSkill(Long id) {
    Optional<Skill> skillById = skillRepository.findById(id);
    if (!skillById.isPresent()) {
      throw new RuntimeException("No Skill found for id:" + id);
    }

    Skill skill = skillById.get();
    skillRepository.delete(skill);
    return skill.getId();
  }


  public AlertNumber newAlertNumber(Long organisationId, String number, String description) {
    AlertNumber alertNumber = new AlertNumber(organisationId, number, description);
    alertNumberRepository.save(alertNumber);
    return alertNumber;
  }

  public AlertNumber editAlertNumber(Long id, String number, String description) {
    Optional<AlertNumber> alertNumberById = alertNumberRepository.findById(id);
    if (!alertNumberById.isPresent()) {
      throw new RuntimeException("No AlertNumber found for id:" + id);
    }

    AlertNumber alertNumber = alertNumberById.get();
    alertNumber.setNumber(number);
    alertNumber.setDescription(description);

    alertNumberRepository.save(alertNumber);
    return alertNumber;
  }

  public Long deleteAlertNumber(Long id) {
    Optional<AlertNumber> alertNumberById = alertNumberRepository.findById(id);
    if (!alertNumberById.isPresent()) {
      throw new RuntimeException("No AlertNumber found for id:" + id);
    }

    AlertNumber alertNumber = alertNumberById.get();
    alertNumberRepository.delete(alertNumber);
    return alertNumber.getId();
  }

  public boolean addEmployeeSkill(Long employeeId, Long skillId) {
    List<EmployeeSkill> existingEmployeeSkill = employeeSkillRepository
        .findByEmployeeIdAndSkillId(employeeId, skillId);

    if (existingEmployeeSkill.size() == 0) {
      employeeSkillRepository.save(new EmployeeSkill(employeeId, skillId));
      return true;
    }
    return false;
  }

  public boolean deleteEmployeeSkill(Long employeeId, Long skillId) {
    List<EmployeeSkill> existingEmployeeSkill = employeeSkillRepository
        .findByEmployeeIdAndSkillId(employeeId, skillId);

    if (existingEmployeeSkill.size() != 0) {
      employeeSkillRepository.deleteAll(existingEmployeeSkill);
      return true;
    }
    return false;
  }
}