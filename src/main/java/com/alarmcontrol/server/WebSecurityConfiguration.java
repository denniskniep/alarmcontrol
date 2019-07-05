package com.alarmcontrol.server;

import com.alarmcontrol.server.auth.ApplicationUserService;
import com.alarmcontrol.server.auth.InitialAdminUserProperties;
import com.alarmcontrol.server.auth.JWTAuthenticationFilter;
import com.alarmcontrol.server.auth.JWTAuthorizationFilter;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({InitialAdminUserProperties.class})
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${security.allowUnauthenticatedH2Console:false}")
  private boolean allowUnauthenticatedH2Console = false;

  @Value("${security.allowUnrestrictedCorsAccess:false}")
  private boolean allowUnrestrictedCorsAccess = false;

  private ApplicationUserService userDetailsService;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public WebSecurityConfiguration(ApplicationUserService userDetailsService,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //Both are necessary for the h2-console

    http.antMatcher("/h2-console/**").csrf().disable();
    http.antMatcher("/h2-console/**").headers().frameOptions().disable();
    if(allowUnauthenticatedH2Console){
      http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
    }

    http.antMatcher("/**").csrf().disable();

    http.authorizeRequests()
        .antMatchers("/login*").permitAll()
        .antMatchers("/app*").permitAll()
        .anyRequest().authenticated();

    http
        .addFilter(new JWTAuthenticationFilter(authenticationManager()))
        .addFilter(new JWTAuthorizationFilter(authenticationManager()));

    // this disables session creation on Spring Security
    http
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    if(allowUnrestrictedCorsAccess){
      http
          .cors().configurationSource(corsConfigurationSource());
    }
  }

  private CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }
}