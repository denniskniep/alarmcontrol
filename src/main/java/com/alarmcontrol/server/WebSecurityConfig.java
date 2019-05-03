package com.alarmcontrol.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration()
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${cors.allow-all}")
  private boolean corsAllowAll;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/**").permitAll();

    http.antMatcher("/**").csrf().disable();
    http.antMatcher("/**").headers().frameOptions().disable();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    if(corsAllowAll){
      source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    }
    return source;
  }

}