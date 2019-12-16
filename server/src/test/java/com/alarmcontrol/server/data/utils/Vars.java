package com.alarmcontrol.server.data.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Vars {
  public static ObjectNode create(){
    return new ObjectMapper().createObjectNode();
  }
}
