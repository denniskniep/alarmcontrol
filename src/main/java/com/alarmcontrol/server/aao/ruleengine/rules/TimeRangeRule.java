package com.alarmcontrol.server.aao.ruleengine.rules;

import com.alarmcontrol.server.aao.config.FeiertagBehaviour;
import com.alarmcontrol.server.aao.config.TimeRange;
import com.alarmcontrol.server.aao.ruleengine.AlertContext;
import com.alarmcontrol.server.aao.ruleengine.Feiertag;
import com.alarmcontrol.server.aao.ruleengine.ReferenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeRangeRule implements Rule {

  private Logger LOG = LoggerFactory.getLogger(TimeRangeRule.class);
  private List<TimeRange> timeRanges;

  public TimeRangeRule(List<TimeRange> timeRanges) {
    this.timeRanges = timeRanges;
  }

  private static LocalDateTime toLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  @Override
  public boolean match(ReferenceContext referenceContext, AlertContext alertContext) {
    LocalDateTime localDateTime = toLocalDateTime(alertContext.getUtcDateTime());
    return match(localDateTime, referenceContext.getFeiertage());
  }

  public boolean match(LocalDateTime localDateTime, List<Feiertag> feiertage) {
    return timeRanges
        .stream()
        .anyMatch(c ->
            match(localDateTime, feiertage, c)
        );
  }

  private boolean match(LocalDateTime localDateTime, List<Feiertag> feiertage, TimeRange timeRange) {
    boolean isFeiertag = feiertageContains(localDateTime, feiertage);

    if(isFeiertag && timeRange.getFeiertagBehaviour() != FeiertagBehaviour.IGNORE){
      return isTimeMatching(localDateTime, timeRange) &&
          timeRange.getFeiertagBehaviour() == FeiertagBehaviour.MATCH;
    }

    return isTimeMatching(localDateTime, timeRange) &&
        isWeekDayMatching(localDateTime, timeRange);
  }

  private boolean feiertageContains(LocalDateTime localDateTime, List<Feiertag> feiertage) {
    for (Feiertag feiertag : feiertage) {
      try{
        LocalDate feiertagDate = LocalDate.parse(feiertag.getDate());
        LocalDate localDate = LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth());
        if(localDate.isEqual(feiertagDate)){
          return true;
        }
      }catch (Exception e){
        LOG.error("Error during parsing of Feiertag '{}'", feiertag.getDate(), e);
      }
    }
    return false;
  }

  private boolean isWeekDayMatching(LocalDateTime localDateTime, TimeRange timeRange) {
    return timeRange.getDaysOfWeek().stream().anyMatch(d -> localDateTime.getDayOfWeek() == d);
  }

  private boolean isTimeMatching(LocalDateTime localDateTime, TimeRange timeRange) {
    LocalTime localTime = LocalTime.of(localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond());

    LocalTime fromLocalTime = LocalTime.of(timeRange.getFromTimeHour(), timeRange.getFromTimeMinute());

    // There is a parse error if we try to parse 24:00 as LocalTime
    // But the ToTime is exclusive and therefore we must have a matching for 23:59
    if (timeRange.getToTimeHour() == 24 && timeRange.getToTimeMinute() == 0) {
      return localTime.equals(fromLocalTime) || localTime.isAfter(fromLocalTime);
    }

    LocalTime toLocalTime = LocalTime.of(timeRange.getToTimeHour(), timeRange.getToTimeMinute());
    return localTime.equals(fromLocalTime) ||
        (localTime.isAfter(fromLocalTime) && localTime.isBefore(toLocalTime));
  }
}
