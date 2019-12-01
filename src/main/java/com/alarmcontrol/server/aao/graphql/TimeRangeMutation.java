package com.alarmcontrol.server.aao.graphql;

import com.alarmcontrol.server.aao.AaoConfigurationService;
import com.alarmcontrol.server.aao.config.FeiertagBehaviour;
import com.alarmcontrol.server.aao.config.TimeRange;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class TimeRangeMutation implements GraphQLMutationResolver {
  private AaoConfigurationService aaoConfigurationService;

  public TimeRangeMutation(AaoConfigurationService aaoConfigurationService) {
    this.aaoConfigurationService = aaoConfigurationService;
  }

  public TimeRange addTimeRange(Long organisationId, String name,
      int fromTimeHour, int fromTimeMinute, int toTimeHour, int toTimeMinute,
      List<DayOfWeek> daysOfWeek, FeiertagBehaviour feiertagBehaviour) {
    TimeRange timeRange = new TimeRange();
    timeRange.setUniqueId(UUID.randomUUID().toString());
    timeRange.setName(name);

    timeRange.setFromTimeHour(fromTimeHour);
    timeRange.setFromTimeMinute(fromTimeMinute);

    timeRange.setToTimeHour(toTimeHour);
    timeRange.setToTimeMinute(toTimeMinute);

    timeRange.setDaysOfWeek(daysOfWeek);
    timeRange.setFeiertagBehaviour(feiertagBehaviour);

    return aaoConfigurationService.addTimeRange(organisationId, timeRange);
  }

  public String deleteTimeRange(Long organisationId, String uniqueTimeRangeId) {
    return aaoConfigurationService.deleteTimeRange(organisationId, uniqueTimeRangeId);
  }
}
