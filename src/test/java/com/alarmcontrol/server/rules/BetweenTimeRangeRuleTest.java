package com.alarmcontrol.server.rules;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class BetweenTimeRangeRuleTest {

    private String from = "2019-03-01 10:00:00";
    private String to = "2019-03-01 12:00:00";

    @Test
    void match_DateInRange() throws ParseException {
        var result = checkDateInRange("2019-03-01 11:00:00");

        assertThat(result).isEqualTo(true);
    }

    @Test
    void match_DateNotInRange() throws ParseException {
        var result = checkDateInRange("2019-03-01 13:00:00");

        assertThat(result).isEqualTo(false);
    }

    @Test
    void match_DateEqualsFromDate_ReturnsTrue() throws ParseException {
        var result = checkDateInRange(from);

        assertThat(result).isEqualTo(true);
    }

    @Test
    void match_DateEqualToDate_ReturnsTrue() throws ParseException {
        var result = checkDateInRange(to);

        assertThat(result).isEqualTo(true);
    }

    private boolean checkDateInRange(String alertDate) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fromDate = parser.parse(from);
        Date toDate = parser.parse(to);
        LocalTime fromTime = AlertContext.toLocalTime(fromDate);
        LocalTime toTime = AlertContext.toLocalTime(toDate);
        var rule = new BetweenTimeRangeRule(fromTime, toTime);
        var dateToCheck = parser.parse(alertDate);
        var timeToCheck = AlertContext.toLocalTime(dateToCheck);
        return rule.match(new AlertContext("H1", timeToCheck, 4711L));
    }
}