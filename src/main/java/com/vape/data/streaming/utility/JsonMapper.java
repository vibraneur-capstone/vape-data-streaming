package com.vape.data.streaming.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JsonMapper {

    private final static ObjectMapper mapper = new ObjectMapper();

    public String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            log.error("toJson failed due to invalid argument provided");
            return "null";
        }
    }

    public <T> T toObject(String json, Class<T> obj) throws JsonProcessingException {
        return mapper.readValue(json, obj);
    }
}
