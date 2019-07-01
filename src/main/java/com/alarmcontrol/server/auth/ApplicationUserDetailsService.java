package com.alarmcontrol.server.auth;

import com.alarmcontrol.server.data.models.ApplicationUser;
import com.alarmcontrol.server.data.repositories.ApplicationUserRepository;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {
  private ApplicationUserRepository applicationUserRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public ApplicationUserDetailsService(ApplicationUserRepository applicationUserRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.applicationUserRepository = applicationUserRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
    if (applicationUser == null) {
      throw new UsernameNotFoundException(username);
    }
    return new User(applicationUser.getUsername(), applicationUser.getPassword(), Collections.emptyList());
  }

  public CreatedUser createUser(String username, String password){
    ApplicationUser applicationUser = new ApplicationUser();
    ApplicationUser existingUser = applicationUserRepository.findByUsername(username);
    if (existingUser != null) {
      throw new IllegalArgumentException("Username already exists!");
    }

    applicationUser.setUsername(username);

    if(StringUtils.isBlank(password)){
      password = generatePassword();
    }

    applicationUser.setPassword(bCryptPasswordEncoder.encode(password));
    applicationUserRepository.save(applicationUser);
    return new CreatedUser(applicationUser, password);
  }

  private String generatePassword() {

    char[] pwChars = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghkmnpqrstuvwxyz23456789!?".toCharArray();
    SecureRandom random = new SecureRandom();
    A[] as = random.ints(10, 0, pwChars.length)
        .mapToObj(i -> pwChars[i]).toArray(new char[]{});

    return new String(password);
  }

  private static class CreatedUser{

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
}
