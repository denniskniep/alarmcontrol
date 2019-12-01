package com.alarmcontrol.server.aao.config;

import java.time.DayOfWeek;
import java.util.List;

public class TimeRange {
  private String uniqueId;
  private String name;

  private int fromTimeHour;
  private int fromTimeMinute;

  private int toTimeHour;
  private int toTimeMinute;

  private List<DayOfWeek> daysOfWeek;
  private FeiertagBehaviour feiertagBehaviour;

  private void validateTime(int hour, int minute) {
    if (hour > 23) {
      throw new IllegalArgumentException("hour is > 23");
    }

    if (minute > 59) {
      throw new IllegalArgumentException("minute is > 59");
    }
  }

  public void validateFromTime() {
    validateTime(fromTimeHour, fromTimeMinute);
  }

  public void validateToTime() {
    if (toTimeHour == 24 && toTimeMinute == 0) {
      return;
    }
    validateTime(toTimeHour, toTimeMinute);
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getFromTimeHour() {
    return fromTimeHour;
  }

  public void setFromTimeHour(int fromTimeHour) {
    this.fromTimeHour = fromTimeHour;
  }

  public int getFromTimeMinute() {
    return fromTimeMinute;
  }

  public void setFromTimeMinute(int fromTimeMinute) {
    this.fromTimeMinute = fromTimeMinute;
  }

  public int getToTimeHour() {
    return toTimeHour;
  }

  public void setToTimeHour(int toTimeHour) {
    this.toTimeHour = toTimeHour;
  }

  public int getToTimeMinute() {
    return toTimeMinute;
  }

  public void setToTimeMinute(int toTimeMinute) {
    this.toTimeMinute = toTimeMinute;
  }

  public List<DayOfWeek> getDaysOfWeek() {
    return daysOfWeek;
  }

  public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
    this.daysOfWeek = daysOfWeek;
  }

  public FeiertagBehaviour getFeiertagBehaviour() {
    return feiertagBehaviour;
  }

  public void setFeiertagBehaviour(
      FeiertagBehaviour feiertagBehaviour) {
    this.feiertagBehaviour = feiertagBehaviour;
  }

  @Override
  public String toString() {
    return "TimeRange{" +
        "uniqueId='" + uniqueId + '\'' +
        ", name='" + name + '\'' +
        ", fromTimeHour=" + fromTimeHour +
        ", fromTimeMinute=" + fromTimeMinute +
        ", toTimeHour=" + toTimeHour +
        ", toTimeMinute=" + toTimeMinute +
        ", daysOfWeek=" + daysOfWeek +
        ", feiertagBehaviour=" + feiertagBehaviour +
        '}';
  }
}
