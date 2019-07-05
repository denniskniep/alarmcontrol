package com.alarmcontrol.server.auth;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CreateAdminUser implements CommandLineRunner {

  private static final String DEFAULT_ADMIN_USERNAME = "admin";
  private Logger logger = LoggerFactory.getLogger(CreateAdminUser.class);
  private ApplicationUserService userDetailsService;
  private InitialAdminUserProperties initialAdminUserProperties;

  public CreateAdminUser(ApplicationUserService userDetailsService,
      InitialAdminUserProperties initialAdminUserProperties) {
    this.userDetailsService = userDetailsService;
    this.initialAdminUserProperties = initialAdminUserProperties;
  }

  @Override
  public void run(String... args) throws Exception {
    String username = StringUtils.isBlank(initialAdminUserProperties.getUsername()) ?
        DEFAULT_ADMIN_USERNAME
        : initialAdminUserProperties.getUsername();

    if (userDetailsService.userExists(username)) {
      logger.info("Adminuser '{}' already exists", username);
      return;
    }

    if (StringUtils.isBlank(initialAdminUserProperties.getPassword())) {
      CreatedUser createdUser = userDetailsService.createUser(username);
      logger.info("#".repeat(60));
      logger.info("Adminuser '{}' created with password '{}'",
          createdUser.getUser().getUsername(),
          createdUser.getClearTextPassword());
      logger.info("#".repeat(60));
    } else {
      CreatedUser createdUser = userDetailsService.createUser(username, initialAdminUserProperties.getPassword());
      logger.info("Adminuser '{}' created", createdUser.getUser().getUsername());
    }
  }
}
