package com.alarmcontrol.server.auth;

import java.security.SecureRandom;
import java.util.stream.Collectors;

public class SecretGenerator {

  private char[] chars;

  public SecretGenerator(char[] chars) {
    this.chars = chars;
  }

  public String generate(long size){
    SecureRandom random = new SecureRandom();
    return random.ints(size, 0, chars.length)
        .mapToObj(i -> String.valueOf(chars[i]))
        .collect(Collectors.joining());
  }

  public static String generate(char[] chars, long size){
    return new SecretGenerator(chars).generate(size);
  }
}
