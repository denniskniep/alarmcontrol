package com.alarmcontrol.server.aao.ruleengine.rules;

import static org.assertj.core.api.Assertions.assertThat;

import com.alarmcontrol.server.aao.ruleengine.Feiertag;
import com.alarmcontrol.server.aao.ruleengine.rules.DateTimeRule.Config;
import com.alarmcontrol.server.aao.ruleengine.rules.DateTimeRule.FeiertagBehaviour;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DateTimeRuleTest {

  private static Stream<Arguments> mondayAndWednesdayAfternoonProvider() {
    return Stream.of(
        Arguments.of(date(2019, 11, 04, 12, 00, 00), false), // MONDAY
        Arguments.of(date(2019, 11, 04, 13, 29, 59), false), // MONDAY
        Arguments.of(date(2019, 11, 04, 13, 30, 00), true),  // MONDAY
        Arguments.of(date(2019, 11, 04, 14, 00, 00), true),  // MONDAY
        Arguments.of(date(2019, 11, 04, 16, 29, 59), true),  // MONDAY
        Arguments.of(date(2019, 11, 04, 16, 30, 00), false), // MONDAY
        Arguments.of(date(2019, 11, 04, 16, 30, 01), false), // MONDAY
        Arguments.of(date(2019, 11, 04, 16, 31, 00), false), // MONDAY

        Arguments.of(date(2019, 11, 05, 12, 00, 00), false), // TUESDAY
        Arguments.of(date(2019, 11, 05, 13, 29, 59), false), // TUESDAY
        Arguments.of(date(2019, 11, 05, 13, 30, 00), false), // TUESDAY
        Arguments.of(date(2019, 11, 05, 14, 00, 00), false), // TUESDAY
        Arguments.of(date(2019, 11, 05, 16, 29, 59), false), // TUESDAY
        Arguments.of(date(2019, 11, 05, 16, 30, 00), false), // TUESDAY
        Arguments.of(date(2019, 11, 05, 16, 30, 01), false), // TUESDAY
        Arguments.of(date(2019, 11, 05, 16, 31, 00), false), // TUESDAY
        Arguments.of(date(2019, 11, 05, 16, 31, 00), false), // TUESDAY

        Arguments.of(date(2019, 11, 06, 12, 00, 00), false), // WEDNESDAY
        Arguments.of(date(2019, 11, 06, 13, 29, 59), false), // WEDNESDAY
        Arguments.of(date(2019, 11, 06, 13, 30, 00), true),  // WEDNESDAY
        Arguments.of(date(2019, 11, 06, 14, 00, 00), true),  // WEDNESDAY
        Arguments.of(date(2019, 11, 06, 16, 29, 59), true),  // WEDNESDAY
        Arguments.of(date(2019, 11, 06, 16, 30, 00), false), // WEDNESDAY
        Arguments.of(date(2019, 11, 06, 16, 30, 01), false), // WEDNESDAY
        Arguments.of(date(2019, 11, 06, 16, 31, 00), false), // WEDNESDAY
        Arguments.of(date(2019, 11, 06, 16, 31, 00), false)  // WEDNESDAY
    );
  }

  @DisplayName("Monday and Wednesday Afternoon")
  @ParameterizedTest(name = "{index} => date={0}, expected={1}")
  @MethodSource("mondayAndWednesdayAfternoonProvider")
  void mondayAndWednesdayAfternoon(LocalDateTime dateTime, boolean expected){
    Config configA = new Config(
        13, 30,
        16, 30,
        Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
        FeiertagBehaviour.IGNORE);

    List<Config> configs = Arrays.asList(configA);

    boolean match = match(configs, dateTime);

    assertThat(match).isEqualTo(expected);
  }

  /*
    TAG   =   Von 05:00 Bis 18:00, MO, DI, MI, DO, FR
    NACHT =   Von 00:00 Bis 05:00, MO, DI, MI, DO, FR
    NACHT =   Von 18:00 Bis 00:00, MO, DI, MI, DO, FR
    WE    =   Von 00:00 Bis 24:00, SA, SO, Feiertag
  */

  private static Stream<Arguments> weekDayProvider() {
    return Stream.of(
        Arguments.of(date(2019, 11,  4,  0,  0,  0), false), // MONDAY
        Arguments.of(date(2019, 11,  4,  4, 59, 59), false), // MONDAY
        Arguments.of(date(2019, 11,  4,  5,  0,  0), true),  // MONDAY
        Arguments.of(date(2019, 11,  4, 12,  0,  0), true),  // MONDAY
        Arguments.of(date(2019, 11,  4, 13, 13, 13), true),  // MONDAY
        Arguments.of(date(2019, 11,  4, 17, 59, 59), true),  // MONDAY
        Arguments.of(date(2019, 11,  4, 18,  0,  0), false), // MONDAY
        Arguments.of(date(2019, 11,  4, 20,  0,  0), false), // MONDAY
        Arguments.of(date(2019, 11,  4, 23, 59, 59), false),  // MONDAY

        Arguments.of(date(2019, 11,  5, 17, 59, 59), true),  // TUESDAY
        Arguments.of(date(2019, 11,  5, 18,  0,  0), false), // TUESDAY

        Arguments.of(date(2019, 11,  6, 17, 59, 59), true),  // WEDNESDAY
        Arguments.of(date(2019, 11,  6, 18,  0,  0), false), // WEDNESDAY

        Arguments.of(date(2019, 11,  7, 17, 59, 59), true),  // THURSDAY
        Arguments.of(date(2019, 11,  7, 18,  0,  0), false), // THURSDAY

        Arguments.of(date(2019, 11,  8, 17, 59, 59), true),  // FRIDAY
        Arguments.of(date(2019, 11,  8, 18,  0,  0), false),  // FRIDAY

        Arguments.of(date(2019, 11,  9, 17, 59, 59), false),  // SATURDAY
        Arguments.of(date(2019, 11,  9, 18,  0,  0), false),  // SATURDAY

        Arguments.of(date(2019, 11, 10, 17, 59, 59), false),  // SUNDAY
        Arguments.of(date(2019, 11, 10, 18,  0,  0), false),  // SUNDAY

        Arguments.of(date(2019, 11, 11,  5,  0,  0), false),  // MONDAY FEIERTAG
        Arguments.of(date(2019, 11, 11, 12,  0,  0), false),  // MONDAY FEIERTAG
        Arguments.of(date(2019, 11, 11, 17, 59, 59), false), // MONDAY FEIERTAG
        Arguments.of(date(2019, 11, 11, 18,  0,  0), false)  // MONDAY FEIERTAG
    );
  }

  @DisplayName("Day: 05:00-18:00, Mo-Fr; NotFeiertag")
  @ParameterizedTest(name = "{index} => date={0}, expected={1}")
  @MethodSource("weekDayProvider")
  void weekDay(LocalDateTime dateTime, boolean expected){
    Config configDay = new Config(
        05, 00,
        18, 00,
        Arrays.asList(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        ),
        FeiertagBehaviour.NO_MATCH);

    List<Config> configs = Arrays.asList(configDay);

    boolean match = match(configs, dateTime);

    assertThat(match).isEqualTo(expected);
  }

  private static Stream<Arguments> weekNightProvider() {
    return Stream.of(
        Arguments.of(date(2019, 11,  4,  0,  0,  0), true), // MONDAY
        Arguments.of(date(2019, 11,  4,  4, 59, 59), true), // MONDAY
        Arguments.of(date(2019, 11,  4,  5,  0,  0), false),  // MONDAY
        Arguments.of(date(2019, 11,  4, 12,  0,  0), false),  // MONDAY
        Arguments.of(date(2019, 11,  4, 13, 13, 13), false),  // MONDAY
        Arguments.of(date(2019, 11,  4, 17, 59, 59), false),  // MONDAY
        Arguments.of(date(2019, 11,  4, 18,  0,  0), true), // MONDAY
        Arguments.of(date(2019, 11,  4, 20,  0,  0), true), // MONDAY
        Arguments.of(date(2019, 11,  4, 23, 59, 59), true),  // MONDAY

        Arguments.of(date(2019, 11,  5, 04, 59, 59), true),  // TUESDAY
        Arguments.of(date(2019, 11,  5, 05, 00, 00), false),  // TUESDAY
        Arguments.of(date(2019, 11,  5, 17, 59, 59), false),  // TUESDAY
        Arguments.of(date(2019, 11,  5, 18,  0,  0), true), // TUESDAY

        Arguments.of(date(2019, 11,  6, 17, 59, 59), false),  // WEDNESDAY
        Arguments.of(date(2019, 11,  6, 18,  0,  0), true), // WEDNESDAY

        Arguments.of(date(2019, 11,  7, 17, 59, 59), false),  // THURSDAY
        Arguments.of(date(2019, 11,  7, 18,  0,  0), true), // THURSDAY

        Arguments.of(date(2019, 11,  8, 17, 59, 59), false),  // FRIDAY
        Arguments.of(date(2019, 11,  8, 18,  0,  0), true),  // FRIDAY

        Arguments.of(date(2019, 11,  9, 17, 59, 59), false),  // SATURDAY
        Arguments.of(date(2019, 11,  9, 18,  0,  0), false),  // SATURDAY

        Arguments.of(date(2019, 11, 10, 17, 59, 59), false),  // SUNDAY
        Arguments.of(date(2019, 11, 10, 18,  0,  0), false),  // SUNDAY

        Arguments.of(date(2019, 11, 11,  5,  0,  0), false),  // MONDAY FEIERTAG
        Arguments.of(date(2019, 11, 11, 12,  0,  0), false),  // MONDAY FEIERTAG
        Arguments.of(date(2019, 11, 11, 17, 59, 59), false), // MONDAY FEIERTAG
        Arguments.of(date(2019, 11, 11, 18,  0,  0), false)  // MONDAY FEIERTAG
    );
  }

  @DisplayName("Night: 00:00-05:00 & 18:00-24:00, Mo-Fr; NotFeiertag")
  @ParameterizedTest(name = "{index} => date={0}, expected={1}")
  @MethodSource("weekNightProvider")
  void weekNight(LocalDateTime dateTime, boolean expected){

    Config configNightA = new Config(
        00, 00,
        05, 00,
        Arrays.asList(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        ),
        FeiertagBehaviour.NO_MATCH);

    Config configNightB = new Config(
        18, 00,
        24, 00,
        Arrays.asList(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        ),
        FeiertagBehaviour.NO_MATCH);

    List<Config> configs = Arrays.asList(configNightA, configNightB);

    boolean match = match(configs, dateTime);

    assertThat(match).isEqualTo(expected);
  }

  private static Stream<Arguments> weekendProvider() {
    return Stream.of(
        Arguments.of(date(2019, 11,  4, 17, 59, 59), false),  // MONDAY
        Arguments.of(date(2019, 11,  4, 18,  0,  0), false), // MONDAY

        Arguments.of(date(2019, 11,  5, 17, 59, 59), false),  // TUESDAY
        Arguments.of(date(2019, 11,  5, 18,  0,  0), false), // TUESDAY

        Arguments.of(date(2019, 11,  6, 17, 59, 59), false),  // WEDNESDAY
        Arguments.of(date(2019, 11,  6, 18,  0,  0), false), // WEDNESDAY

        Arguments.of(date(2019, 11,  7, 17, 59, 59), false),  // THURSDAY
        Arguments.of(date(2019, 11,  7, 18,  0,  0), false), // THURSDAY

        Arguments.of(date(2019, 11,  8, 17, 59, 59), false),  // FRIDAY
        Arguments.of(date(2019, 11,  8, 18,  0,  0), false),  // FRIDAY

        Arguments.of(date(2019, 11,  9,  0,  0,  0), true),  // SATURDAY
        Arguments.of(date(2019, 11,  9, 17, 59, 59), true),  // SATURDAY
        Arguments.of(date(2019, 11,  9, 18,  0,  0), true),  // SATURDAY
        Arguments.of(date(2019, 11,  9, 23, 59, 59), true),  // SATURDAY

        Arguments.of(date(2019, 11, 10,  0,  0,  0), true),  // SUNDAY
        Arguments.of(date(2019, 11, 10, 17, 59, 59), true),  // SUNDAY
        Arguments.of(date(2019, 11, 10, 18,  0,  0), true),  // SUNDAY
        Arguments.of(date(2019, 11, 10, 23, 59, 59), true),  // SUNDAY

        Arguments.of(date(2019, 11, 11,  5,  0,  0), true),  // MONDAY FEIERTAG
        Arguments.of(date(2019, 11, 11, 12,  0,  0), true),  // MONDAY FEIERTAG
        Arguments.of(date(2019, 11, 11, 17, 59, 59), true), // MONDAY FEIERTAG
        Arguments.of(date(2019, 11, 11, 18,  0,  0), true)  // MONDAY FEIERTAG
    );
  }

  @DisplayName("WE: 00:00-24:00, Sa+So+Feiertag")
  @ParameterizedTest(name = "{index} => date={0}, expected={1}")
  @MethodSource("weekendProvider")
  void weekend(LocalDateTime dateTime, boolean expected){

    Config configWE = new Config(
        00, 00,
        24, 00,
        Arrays.asList(
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        ),
        FeiertagBehaviour.MATCH);

    List<Config> configs = Arrays.asList(configWE);

    boolean match = match(configs, dateTime);

    assertThat(match).isEqualTo(expected);
  }

  @NotNull
  private static LocalDateTime date(int year, int month, int day, int hour, int minute, int second) {
    return LocalDateTime.of(year, month, day, hour, minute, second);
  }

  private boolean match(List<Config> configs, LocalDateTime dateTime) {
    DateTimeRule dateTimeRule = new DateTimeRule(configs);
    return dateTimeRule.match(dateTime, Arrays.asList(new Feiertag("2019-11-11")));
  }
}