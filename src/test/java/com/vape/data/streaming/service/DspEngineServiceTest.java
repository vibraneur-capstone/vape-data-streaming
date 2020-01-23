package com.vape.data.streaming.service;

import com.vape.data.streaming.config.DspEngineConfig;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.dsp.integration.swagger.v1.model.DspDataInput;
import com.vape.dsp.integration.swagger.v1.model.SingleDigitDspDataOutput;
import com.vape.dsp.integration.swagger.v1.model.SingleDigitResultEncapsulation;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class DspEngineServiceTest {

    @Spy
    @InjectMocks
    private DspEngineService serviceToTest;

    @Mock
    private DspEngineConfig config;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("should make service call to dsp kurtosis endpoint and return data")
    void test_computeKurtosis_good_response() {
        // Arrange
        SensorDataPointModel incomingSensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        List<BigDecimal> data= new ArrayList<>();
        data.add(new BigDecimal(321));
        data.add(new BigDecimal(123));
        data.add(new BigDecimal(312));
        DspDataInput dspDataInput = new DspDataInput().data(data);
        HttpEntity<DspDataInput> expectedEntity = new HttpEntity<>(dspDataInput);
        String kurtisisUri = "https://api.lambda.dsp-engine.vibraneur.com/v1/algorithms/kurtosis";

        BigDecimal expectedResult = new BigDecimal(1.5);

        doReturn(expectedEntity).when(serviceToTest).getRequestEntity(incomingSensorDataPointModel);
        when(config.getKurtosis()).thenReturn(kurtisisUri);

        // Act
        BigDecimal actualResult = serviceToTest.computeKurtosis(incomingSensorDataPointModel);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(expectedResult.doubleValue(), actualResult.doubleValue())
                );
    }

    @Test
    @DisplayName("should make service call to dsp RMS endpoint and return data")
    void test_computeRMS_good_response() {
        // Arrange
        SensorDataPointModel incomingSensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        List<BigDecimal> data= new ArrayList<>();
        data.add(new BigDecimal(321));
        data.add(new BigDecimal(213));
        data.add(new BigDecimal(312));
        DspDataInput dspDataInput = new DspDataInput().data(data);
        HttpEntity<DspDataInput> expectedEntity = new HttpEntity<>(dspDataInput);
        String rmsUri = "https://api.lambda.dsp-engine.vibraneur.com/v1/algorithms/rms";

        BigDecimal expectedResult = new BigDecimal(286.21320724243316);

        doReturn(expectedEntity).when(serviceToTest).getRequestEntity(incomingSensorDataPointModel);
        when(config.getRms()).thenReturn(rmsUri);

        // Act
        BigDecimal actualResult = serviceToTest.computeRMS(incomingSensorDataPointModel);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(expectedResult.doubleValue(), actualResult.doubleValue())
        );
    }

    @Test
    @DisplayName("shoud return HttpEntity")
    void test_getRequestEntity() {
        // Arrange
        List<BigDecimal> data= new ArrayList<>();
        data.add(new BigDecimal(321));
        data.add(new BigDecimal(213));
        data.add(new BigDecimal(312));
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().data(data).build();

        // Act
        HttpEntity<DspDataInput> actualEntity = serviceToTest.getRequestEntity(sensorDataPointModel);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(data, actualEntity.getBody().getData())
        );
    }
}
