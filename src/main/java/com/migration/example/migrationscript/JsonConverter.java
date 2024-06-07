package com.migration.example.migrationscript;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String convertToJson(UserData userData) throws JsonProcessingException {
        return objectMapper.writeValueAsString(userData);
    }
}

