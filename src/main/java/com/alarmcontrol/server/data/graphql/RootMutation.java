package com.alarmcontrol.server.data.graphql;

import com.alarmcontrol.server.data.AlertService;
import com.alarmcontrol.server.data.graphql.alert.EmployeeFeedbackForAlertAddedPublisher;
import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.AlertEmployee;
import com.alarmcontrol.server.data.models.Employee;
import com.alarmcontrol.server.data.models.EmployeeSkill;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.models.Skill;
import com.alarmcontrol.server.data.repositories.AlertEmployeeRepository;
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
  private AlertEmployeeRepository alertEmployeeRepository;
  private SkillRepository skillRepository;
  private EmployeeSkillRepository employeeSkillRepository;
  private EmployeeFeedbackForAlertAddedPublisher employeeFeedbackForAlertAddedPublisher;

  public RootMutation(AlertService alertService,
      OrganisationRepository organisationRepository,
      EmployeeRepository employeeRepository,
      AlertEmployeeRepository alertEmployeeRepository,
      SkillRepository skillRepository,
      EmployeeSkillRepository employeeSkillRepository,
      EmployeeFeedbackForAlertAddedPublisher employeeFeedbackForAlertAddedPublisher) {
    this.alertService = alertService;
    this.organisationRepository = organisationRepository;
    this.employeeRepository = employeeRepository;
    this.alertEmployeeRepository = alertEmployeeRepository;
    this.skillRepository = skillRepository;
    this.employeeSkillRepository = employeeSkillRepository;
    this.employeeFeedbackForAlertAddedPublisher = employeeFeedbackForAlertAddedPublisher;
  }

  public Alert newAlert(Long organisationId,
      String keyword,
      Date dateTime,
      String description,
      String address) {
    return alertService.create(organisationId, keyword, dateTime, description, address);
  }

  public AlertEmployee setEmployeeFeedbackForAlert(Long employeeId, Long alertId, AlertEmployee.Feedback feedback) {
    List<AlertEmployee> existingFeedback = alertEmployeeRepository
        .findByAlertIdAndEmployeeId(alertId, employeeId);

    if(existingFeedback.size() > 1){
      throw new RuntimeException("There should be at most one existing feedback per alert and employee, but "
          +existingFeedback.size()+ " were found");
    }

    AlertEmployee alertEmployee;
    if(existingFeedback.size() == 1){
      alertEmployee = existingFeedback.get(0);
    }else{
      alertEmployee = new AlertEmployee(employeeId, alertId, feedback, new Date());
    }
    alertEmployee.setFeedback(feedback);
    employeeFeedbackForAlertAddedPublisher.emitEmployeeFeedbackForAlertAdded(alertEmployee.getAlertId(), alertEmployee.getEmployeeId());
    return alertEmployeeRepository.save(alertEmployee);
  }

  public Employee newEmployee(Long organisationId,
      String firstname,
      String lastname) {
    Employee employee = new Employee(organisationId, firstname, lastname);
    return employeeRepository.save(employee);
  }

  public Long deleteEmployee(Long id) {
    Optional<Employee> employeeById = employeeRepository.findById(id);
    if(!employeeById.isPresent()){
      throw new RuntimeException("No Employee found for id:" +id);
    }

    Employee employee = employeeById.get();
    employeeRepository.delete(employee);
    return employee.getId();
  }

  public Employee editEmployee(Long id,
      String firstname,
      String lastname) {

    Optional<Employee> employeeById = employeeRepository.findById(id);
    if(!employeeById.isPresent()){
      throw new RuntimeException("No Employee found for id:" +id);
    }

    Employee employee = employeeById.get();
    employee.setFirstname(firstname);
    employee.setLastname(lastname);

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
    if(!organisationById.isPresent()){
      throw new RuntimeException("No Organisation found for id:"+id);
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
    if(!organisationById.isPresent()){
      throw new RuntimeException("No Organisation found for id:" +id);
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
    if(!skillById.isPresent()){
      throw new RuntimeException("No Skill found for id:" +id);
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
    if(!skillById.isPresent()){
      throw new RuntimeException("No Skill found for id:" +id);
    }

    Skill skill = skillById.get();
    skillRepository.delete(skill);
    return skill.getId();
  }

  public boolean addEmployeeSkill(Long employeeId, Long skillId) {
    List<EmployeeSkill> existingEmployeeSkill = employeeSkillRepository
        .findByEmployeeIdAndSkillId(employeeId, skillId);

    if(existingEmployeeSkill.size() == 0) {
      employeeSkillRepository.save(new EmployeeSkill(employeeId, skillId));
      return true;
    }
    return false;
  }
}