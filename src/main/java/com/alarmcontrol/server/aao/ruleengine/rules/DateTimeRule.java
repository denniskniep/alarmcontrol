package com.alarmcontrol.server.aao.ruleengine.rules;

import com.alarmcontrol.server.aao.ruleengine.AlertContext;
import com.alarmcontrol.server.aao.ruleengine.Feiertag;
import com.alarmcontrol.server.aao.ruleengine.ReferenceContext;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateTimeRule implements Rule {

  private Logger LOG = LoggerFactory.getLogger(DateTimeRule.class);
  private List<Config> configs;

  public DateTimeRule(List<Config> configs) {
    this.configs = configs;
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
    return configs
        .stream()
        .anyMatch(c ->
            match(localDateTime, feiertage, c)
        );
  }

  private boolean match(LocalDateTime localDateTime, List<Feiertag> feiertage, Config config) {
    boolean isFeiertag = feiertageContains(localDateTime, feiertage);

    if(isFeiertag && config.getFeiertagBehaviour() != FeiertagBehaviour.IGNORE){
      return isTimeMatching(localDateTime, config) &&
          config.getFeiertagBehaviour() == FeiertagBehaviour.MATCH;
    }

    return isTimeMatching(localDateTime, config) &&
        isWeekDayMatching(localDateTime, config);
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

  private boolean isWeekDayMatching(LocalDateTime localDateTime, Config c) {
    return c.getDaysOfWeek().stream().anyMatch(d -> localDateTime.getDayOfWeek() == d);
  }

  private boolean isTimeMatching(LocalDateTime localDateTime, Config config) {
    LocalTime localTime = LocalTime.of(localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond());

    LocalTime fromLocalTime = LocalTime.of(config.getFromTimeHour(), config.getFromTimeMinute());

    // There is a parse error if we try to parse 24:00 as LocalTime
    // But the ToTime is exclusive and therefore we must have a matching for 23:59
    if (config.getToTimeHour() == 24 && config.getToTimeMinute() == 0) {
      return localTime.equals(fromLocalTime) || localTime.isAfter(fromLocalTime);
    }

    LocalTime toLocalTime = LocalTime.of(config.getToTimeHour(), config.getToTimeMinute());
    return localTime.equals(fromLocalTime) ||
        (localTime.isAfter(fromLocalTime) && localTime.isBefore(toLocalTime));
  }

  public enum FeiertagBehaviour {
    IGNORE,
    MATCH,
    NO_MATCH
  }

  public static class Config {

    private final int fromTimeHour;
    private final int fromTimeMinute;

    private final int toTimeHour;
    private final int toTimeMinute;

    private final List<DayOfWeek> daysOfWeek;
    private final FeiertagBehaviour feiertagBehaviour;

    public Config(int fromTimeHour, int fromTimeMinute, int toTimeHour, int toTimeMinute,
        List<String> daysOfWeek, String feiertagBehaviour) {
      this(fromTimeHour,
          fromTimeMinute,
          toTimeHour,
          toTimeMinute,
          daysOfWeek.stream().map(d -> DayOfWeek.valueOf(d)).collect(Collectors.toList()),
          FeiertagBehaviour.valueOf(feiertagBehaviour));
    }

    public Config(int fromTimeHour, int fromTimeMinute, int toTimeHour, int toTimeMinute, List<DayOfWeek> daysOfWeek,
        FeiertagBehaviour feiertagBehaviour) {

      validateTime(fromTimeHour, fromTimeMinute);
      validateToTime(toTimeHour, toTimeMinute);

      this.fromTimeHour = fromTimeHour;
      this.fromTimeMinute = fromTimeMinute;
      this.toTimeHour = toTimeHour;
      this.toTimeMinute = toTimeMinute;

      this.daysOfWeek = daysOfWeek;
      this.feiertagBehaviour = feiertagBehaviour;
    }

    private void validateTime(int hour, int minute) {
      if (hour > 23) {
        throw new IllegalArgumentException("hour is > 23");
      }

      if (minute > 59) {
        throw new IllegalArgumentException("minute is > 59");
      }
    }

    private void validateToTime(int hour, int minute) {
      if (hour == 24 && minute == 0) {
        return;
      }
      validateTime(hour, minute);
    }

    public int getFromTimeHour() {
      return fromTimeHour;
    }

    public int getFromTimeMinute() {
      return fromTimeMinute;
    }

    public int getToTimeHour() {
      return toTimeHour;
    }

    public int getToTimeMinute() {
      return toTimeMinute;
    }

    public List<DayOfWeek> getDaysOfWeek() {
      return daysOfWeek;
    }

    public FeiertagBehaviour getFeiertagBehaviour() {
      return feiertagBehaviour;
    }
  }
}
