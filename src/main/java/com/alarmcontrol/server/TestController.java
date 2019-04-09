package com.alarmcontrol.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @RequestMapping("/api/test")
  public String index() {
    return "{\"greeting\" : \"Guten Tag\" }";
  }
}
