package com.alarmcontrol.server.auth;

import com.alarmcontrol.server.data.models.ApplicationUser;

public class CreatedUser{

  private ApplicationUser user;
  private String clearTextPassword;

  public CreatedUser(ApplicationUser user, String clearTextPassword) {
    this.user = user;
    this.clearTextPassword = clearTextPassword;
  }

  public ApplicationUser getUser() {
    return user;
  }

  public String getClearTextPassword() {
    return clearTextPassword;
  }
}
