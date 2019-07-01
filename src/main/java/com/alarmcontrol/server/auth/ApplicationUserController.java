package com.alarmcontrol.server.auth;

import com.alarmcontrol.server.data.models.ApplicationUser;
import com.alarmcontrol.server.data.repositories.ApplicationUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

  @RestController
  @RequestMapping("/users")
  public class ApplicationUserController {

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApplicationUserController(ApplicationUserRepository applicationUserRepository,
        BCryptPasswordEncoder bCryptPasswordEncoder) {
      this.applicationUserRepository = applicationUserRepository;
      this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody ApplicationUser user) {
      user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
      applicationUserRepository.save(user);
    }
  }