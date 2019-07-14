package com.alarmcontrol.server.data.graphql;

import com.alarmcontrol.server.rules.data.BetweenTimeRangeRuleData;
import com.coxautodev.graphql.tools.SchemaParserDictionary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class GraphQlConfiguration {
    @Bean
    public SchemaParserDictionary getSchemaParser() {
        SchemaParserDictionary dictionary = new SchemaParserDictionary();
        dictionary.add(new HashMap() {{
            put(BetweenTimeRangeRuleData.CLASSNAME, BetweenTimeRangeRuleData.class);
        }});
        return dictionary;
    }
}
