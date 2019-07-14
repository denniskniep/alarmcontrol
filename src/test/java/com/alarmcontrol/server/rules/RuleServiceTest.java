package com.alarmcontrol.server.rules;

import com.alarmcontrol.server.data.TestConfiguration;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.alarmcontrol.server.data.utils.GraphQLClient;
import com.alarmcontrol.server.data.utils.TestOrganisation;
import com.alarmcontrol.server.rules.data.BetweenTimeRangeRuleData;
import com.alarmcontrol.server.rules.data.RuleContainerData;
import com.alarmcontrol.server.rules.data.RuleContainerDataCollection;
import com.alarmcontrol.server.rules.data.RuleData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
class RuleServiceTest {
    @Autowired
    private GraphQLClient graphQlClient;

    @Autowired
    private RuleService ruleService;

    private String from = "2019-03-01 10:00:00";
    private String to = "2019-03-01 12:00:00";

    @Test
    void evaluateAao_returnsExpectedResult() throws ParseException, JsonProcessingException {
       /* var testOrganisation = setupOrganisation();
        var ruleContainerDataCollection = new RuleContainerDataCollection();
        var ruleContainers = new ArrayList<RuleContainerData>();
        var ruleContainerData = new RuleContainerData();
        var ruleData = new BetweenTimeRangeRuleData();
        var results = new ArrayList<String>();
        results.add("HLF");
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fromDate = parser.parse(from);
        ruleData.setFrom(LocalTime.parse("10:00:00"));
        ruleData.setTo(LocalTime.parse("12:00:00"));
        var ruleDatas = new ArrayList<RuleData>();
        ruleDatas.add(ruleData);
        ruleContainerData.setRules(ruleDatas);
        ruleContainers.add(ruleContainerData);
        ruleContainerData.setResults(results);
        ruleContainerDataCollection.setRuleContainers(ruleContainers);

        var json = new ObjectMapper().writeValueAsString(ruleContainerDataCollection);
        System.out.println();*/
        var testOrganisation = setupOrganisation();
        var json = "{\n" +
                "  \"ruleContainers\": [\n" +
                "    {\n" +
                "      \"rules\": [\n" +
                "        {\n" +
                "          \"type\": \"BetweenTimeRange\",\n" +
                "          \"from\": \"10:00\",\n" +
                "          \"to\": \"12:00\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"results\": [\n" +
                "        \"HLF\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ruleService.saveAaoRules(testOrganisation.getId(), json);
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LocalTime alertTime = AlertContext.toLocalTime(parser.parse(to));
        alertTime.minusMinutes(30);
        var result = ruleService.evaluateAao(new AlertContext("H1", alertTime, testOrganisation.getId()));

        assertThat(result.getResults()).containsExactly("HLF");
    }


    /*

    var testOrganisation = setupOrganisation();
        var ruleContainerDataCollection = new RuleContainerDataCollection();
        var ruleContainers = new ArrayList<RuleContainerData>();
        var ruleContainerData = new RuleContainerData();
        var ruleData = new BetweenTimeRangeRuleData();
        var results = new ArrayList<String>();
        results.add("HLF");
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fromDate = parser.parse(from);
        ruleData.setFrom(fromDate);
        Date toDate = parser.parse(to);
        ruleData.setTo(toDate);
        var ruleDatas = new ArrayList<RuleData>();
        ruleDatas.add(ruleData);
        ruleContainerData.setRules(ruleDatas);
        ruleContainers.add(ruleContainerData);
        ruleContainerData.setResults(results);
        ruleContainerDataCollection.setRuleContainers(ruleContainers);

        var json = new ObjectMapper().writeValueAsString(ruleContainerDataCollection);
     */
    private TestOrganisation setupOrganisation() {
        TestOrganisation org = graphQlClient.createOrganisation("Organisation" + UUID.randomUUID());
        return org;
    }
}