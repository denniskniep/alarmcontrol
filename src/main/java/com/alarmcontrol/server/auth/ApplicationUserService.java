package com.alarmcontrol.server.auth;

import com.alarmcontrol.server.data.models.ApplicationUser;
import com.alarmcontrol.server.data.repositories.ApplicationUserRepository;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

  private static char[] PASSWORD_CHARS = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghkmnpqrstuvwxyz23456789!?".toCharArray();
  private ApplicationUserRepository applicationUserRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public ApplicationUserService(ApplicationUserRepository applicationUserRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.applicationUserRepository = applicationUserRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  public boolean userExists(String username){
    username = normalize(username);
    return applicationUserRepository.findByUsername(username) != null;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    username = normalize(username);
    ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
    if (applicationUser == null) {
      throw new UsernameNotFoundException(username);
    }
    return new User(applicationUser.getUsername(), applicationUser.getPassword(), Collections.emptyList());
  }

  public CreatedUser createUser(String username) {
    return createUser(username, null);
  }

  public CreatedUser createUser(String username, String password) {
    username = normalize(username);
    ApplicationUser existingUser = applicationUserRepository.findByUsername(username);
    if (existingUser != null) {
      throw new IllegalArgumentException("Username already exists!");
    }

    ApplicationUser applicationUser = new ApplicationUser();
    applicationUser.setUsername(username);

    if (StringUtils.isBlank(password)) {
      password = generatePassword();
    }
    applicationUser.setPassword(bCryptPasswordEncoder.encode(password));
    applicationUserRepository.save(applicationUser);
    return new CreatedUser(applicationUser, password);
  }

  private String generatePassword() {
    return SecretGenerator.generate(PASSWORD_CHARS, 18);
  }

  private String normalize(String username) {
    return username.toLowerCase();
  }
}
