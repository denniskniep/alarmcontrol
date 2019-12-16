package com.alarmcontrol.server.data.models;

public enum Feedback {
  NO_RESPONSE(0),
  COMMIT(1),
  LATER(2),
  CANCEL (3);

  private int value;

  Feedback(int value) { this.value = value; }

  public int getValue() { return value; }

  public static Feedback parse(int id) {
    Feedback feedback = null;
    for (Feedback item : Feedback.values()) {
      if (item.getValue()==id) {
        feedback = item;
        break;
      }
    }
    return feedback;
  }
}
