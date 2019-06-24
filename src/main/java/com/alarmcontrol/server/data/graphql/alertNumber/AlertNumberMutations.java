package com.alarmcontrol.server.data.graphql.alertNumber;

import com.alarmcontrol.server.data.models.AlertNumber;
import com.alarmcontrol.server.data.repositories.AlertNumberRepository;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AlertNumberMutations implements GraphQLMutationResolver {

  private AlertNumberRepository alertNumberRepository;

  public AlertNumberMutations(AlertNumberRepository alertNumberRepository) {
    this.alertNumberRepository = alertNumberRepository;
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
}
