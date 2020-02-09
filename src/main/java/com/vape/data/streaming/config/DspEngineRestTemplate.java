package com.vape.data.streaming.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Data
@RequiredArgsConstructor
public class DspEngineRestTemplate {

    private final DspEngineConfig config;

    private final RestTemplate restTemplate = new RestTemplate();

    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("x-api-key", config.getApiKey());
        return headers;
    }

}
