package com.alarmcontrol.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SPAController {

  @RequestMapping({"/"})
  public RedirectView root() {
    return new RedirectView("/app");
  }

  @RequestMapping({"/app", "/app/**"})
  public String welcome() {
    return "/index.html";
  }

}
