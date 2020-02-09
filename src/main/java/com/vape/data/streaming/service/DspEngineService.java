package com.vape.data.streaming.service;

import com.vape.data.streaming.config.DspEngineConfig;
import com.vape.data.streaming.config.DspEngineRestTemplate;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.dsp.integration.swagger.v1.model.DspDataInput;
import com.vape.dsp.integration.swagger.v1.model.SingleDigitDspDataOutput;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class DspEngineService {

    private final DspEngineConfig config;

    private final DspEngineRestTemplate dspEngineRestTemplate;

    public BigDecimal computeKurtosis(SensorDataPointModel sensorDataPoint) {
        String uri = config.getKurtosis();
        HttpEntity entity = getRequestEntity(sensorDataPoint);
        ResponseEntity<SingleDigitDspDataOutput> result = dspEngineRestTemplate.getRestTemplate().exchange(uri, HttpMethod.POST, entity, SingleDigitDspDataOutput.class);
        return result.getBody().getBody().getResult();
    }

    public BigDecimal computeRMS(SensorDataPointModel sensorDataPoint) throws IllegalStateException {
        String uri = config.getRms();
        HttpEntity entity = getRequestEntity(sensorDataPoint);
        ResponseEntity<SingleDigitDspDataOutput> result = dspEngineRestTemplate.getRestTemplate().exchange(uri, HttpMethod.POST, entity, SingleDigitDspDataOutput.class);
        return result.getBody().getBody().getResult();
    }

    HttpEntity<DspDataInput> getRequestEntity(SensorDataPointModel sensorDataPoint) {
        List<BigDecimal> dataPoint = sensorDataPoint.getData().stream().map(BigDecimal::new).collect(Collectors.toList());
        return new HttpEntity<>(new DspDataInput().data(dataPoint), dspEngineRestTemplate.getHeaders());
    }
}
