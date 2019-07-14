package com.alarmcontrol.server.rules.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

    @Override
    public void serialize(
            LocalTime time,
            JsonGenerator gen,
            SerializerProvider arg2) throws IOException {
        gen.writeString(time.toString());
    }
}
