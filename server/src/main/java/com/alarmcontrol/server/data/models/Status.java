package com.alarmcontrol.server.data.models;

public enum Status {
  NOT_AVAILABLE(0),
  AVAILABLE(1);

  private int value;

  Status(int value) {
    this.value = value;
  }

  public static Status parse(int id) {
    Status status = null;
    for (Status item : Status.values()) {
      if (item.getValue() == id) {
        status = item;
        break;
      }
    }
    return status;
  }

  public int getValue() {
    return value;
  }
}