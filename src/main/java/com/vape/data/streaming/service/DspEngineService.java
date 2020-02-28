package com.vape.data.streaming.service;

import com.vape.data.streaming.config.DspEngineConfig;
import com.vape.data.streaming.config.DspEngineRestTemplate;
import com.vape.data.streaming.model.DspTopic;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.dsp.integration.swagger.v1.model.ComplexNumberResultEncapsulation;
import com.vape.dsp.integration.swagger.v1.model.DspDataInput;
import com.vape.dsp.integration.swagger.v1.model.SingleDigitDspDataOutput;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class DspEngineService {

    private final DspEngineConfig config;

    private final DspEngineRestTemplate dspEngineRestTemplate;

    List<Double> computeFreqDomain(DspTopic topic, SensorDataPointModel sensorDataPoint) {
        ResponseEntity<ComplexNumberResultEncapsulation> response = invokeDspEngineFreqDomain(config.getUriByDspTopic(topic), sensorDataPoint);
        return isResponseOk(response)
                ? response.getBody().getResult().stream().map(Double::valueOf).collect(Collectors.toList())
                : null;
    }

    Double computeTimeDomain(DspTopic topic, SensorDataPointModel sensorDataPoint) {
        ResponseEntity<SingleDigitDspDataOutput> response = invokeDspEngineTimeDomain(config.getUriByDspTopic(topic), sensorDataPoint);
        return isResponseOk(response) ? Objects.requireNonNull(response.getBody()).getBody().getResult().doubleValue() : null;
    }

    private ResponseEntity<SingleDigitDspDataOutput> invokeDspEngineTimeDomain(String uri, SensorDataPointModel sensorDataPoint) {
        HttpEntity entity = getRequestEntity(sensorDataPoint);
        return dspEngineRestTemplate.getRestTemplate().exchange(uri, HttpMethod.POST, entity, SingleDigitDspDataOutput.class);
    }

    private ResponseEntity<ComplexNumberResultEncapsulation> invokeDspEngineFreqDomain(String uri, SensorDataPointModel sensorDataPoint) {
        HttpEntity entity = getRequestEntity(sensorDataPoint);
        return dspEngineRestTemplate.getRestTemplate().exchange(uri, HttpMethod.POST, entity, ComplexNumberResultEncapsulation.class);
    }

    private boolean isResponseOk(ResponseEntity res) {
        if (res.getStatusCode() == HttpStatus.OK && res.getBody() != null) {
            log.info("#### DSP RETURNED OK ####");
            return true;
        } else {
            log.error("DSP RETURNED STATUS OF " + res.getStatusCodeValue());
            return false;
        }
    }

    HttpEntity<DspDataInput> getRequestEntity(SensorDataPointModel sensorDataPoint) {
        List<BigDecimal> dataPoint = sensorDataPoint.getData().stream().map(BigDecimal::new).collect(Collectors.toList());
        return new HttpEntity<>(new DspDataInput().data(dataPoint), dspEngineRestTemplate.getHeaders());
    }
}
