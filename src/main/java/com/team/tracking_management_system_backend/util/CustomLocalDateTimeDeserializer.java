package com.team.tracking_management_system_backend.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatPattern);
//        LocalDate localDate = LocalDate.parse(source, dateTimeFormatter);
//        return localDate.atStartOfDay();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(p.getText(),dateTimeFormatter);
        return localDate.atStartOfDay();

//        return LocalDateTime.parse(p.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
