package com.vape.data.streaming.service;

import com.vape.data.streaming.config.DspEngineConfig;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.dsp.integration.swagger.v1.model.DspDataInput;
import com.vape.dsp.integration.swagger.v1.model.SingleDigitDspDataOutput;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@Slf4j
@AllArgsConstructor
public class DspEngineService {

    private final DspEngineConfig config;

    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal computeKurtosis(SensorDataPointModel sensorDataPoint) {
        HttpEntity entity = getRequestEntity(sensorDataPoint);
        ResponseEntity<SingleDigitDspDataOutput> result = restTemplate.exchange(config.getKurtosis(), HttpMethod.POST, entity, SingleDigitDspDataOutput.class);
        if (result.getBody() != null && result.getBody().getBody() != null) {
            return result.getBody().getBody().getResult();
        }
        else {
            log.error("null body returned from dsp api call for Kurtosis");
            throw new IllegalStateException("null body returned from dsp api call for Kurtosis");
        }
    }

    public BigDecimal computeRMS(SensorDataPointModel sensorDataPoint) throws IllegalStateException {
        HttpEntity entity = getRequestEntity(sensorDataPoint);
        ResponseEntity<SingleDigitDspDataOutput> result = restTemplate.exchange(config.getRms(), HttpMethod.POST, entity, SingleDigitDspDataOutput.class);
        if(result.getBody() != null && result.getBody().getBody() != null) {
            return result.getBody().getBody().getResult();
        }
        else {
            log.error("null body returned from dsp api call for RMS");
            throw new IllegalStateException("null body returned from dsp api call for RMS");
        }
    }

    HttpEntity<DspDataInput> getRequestEntity(SensorDataPointModel sensorDataPoint) {
        // TODO: Add Header
        return new HttpEntity<>(new DspDataInput().data(sensorDataPoint.getData()));
    }
}
