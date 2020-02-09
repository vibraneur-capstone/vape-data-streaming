package com.vape.data.streaming.service;

import com.vape.data.streaming.config.DspEngineConfig;
import com.vape.data.streaming.config.DspEngineRestTemplate;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.dsp.integration.swagger.v1.model.DspDataInput;
import com.vape.dsp.integration.swagger.v1.model.SingleDigitDspDataOutput;
import com.vape.dsp.integration.swagger.v1.model.SingleDigitResultEncapsulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DspEngineServiceTest {

    @Spy
    @InjectMocks
    private DspEngineService serviceToTest;

    @Mock
    private DspEngineRestTemplate restTemplate;

    @Mock
    private DspEngineConfig config;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private final RestTemplate mockRestTemplate = mock(RestTemplate.class);

    @Test
    @DisplayName("should make service call to dsp kurtosis endpoint and return data")
    void test_computeKurtosis_good_response() {
        // Arrange
        SensorDataPointModel incomingSensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        DspDataInput dspDataInput = new DspDataInput().data(new ArrayList<>());
        HttpEntity<DspDataInput> expectedEntity = new HttpEntity<>(dspDataInput);
        String kurtisisUri = "https://benxin.is.the.best.com/kurtosis";

        BigDecimal expectedResult = new BigDecimal(1.3);
        ResponseEntity<SingleDigitDspDataOutput> expectedResponse = new ResponseEntity<>(new SingleDigitDspDataOutput().body(new SingleDigitResultEncapsulation().result(expectedResult)), HttpStatus.OK);

        doReturn(expectedEntity).when(serviceToTest).getRequestEntity(incomingSensorDataPointModel);
        when(config.getKurtosis()).thenReturn(kurtisisUri);
        when(restTemplate.getRestTemplate()).thenReturn(mockRestTemplate);
        when(mockRestTemplate.exchange(kurtisisUri, HttpMethod.POST, expectedEntity, SingleDigitDspDataOutput.class)).thenReturn(expectedResponse);

        // Act
        BigDecimal actualResult = serviceToTest.computeKurtosis(incomingSensorDataPointModel);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(expectedResult.doubleValue(), actualResult.doubleValue())
                );

        verify(restTemplate, times(1)).getRestTemplate();
        verify(mockRestTemplate, times(1)).exchange(kurtisisUri, HttpMethod.POST, expectedEntity, SingleDigitDspDataOutput.class);
    }

    @Test
    @DisplayName("should make service call to dsp RMS endpoint and return data")
    void test_computeRMS_good_response() {
        // Arrange
        SensorDataPointModel incomingSensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        DspDataInput dspDataInput = new DspDataInput().data(new ArrayList<>());
        HttpEntity<DspDataInput> expectedEntity = new HttpEntity<>(dspDataInput);
        String rmsUri = "https://benxin.is.the.best.com/rms";

        BigDecimal expectedResult = new BigDecimal(2);
        ResponseEntity<SingleDigitDspDataOutput> expectedResponse = new ResponseEntity<>(new SingleDigitDspDataOutput().body(new SingleDigitResultEncapsulation().result(expectedResult)), HttpStatus.OK);


        doReturn(expectedEntity).when(serviceToTest).getRequestEntity(incomingSensorDataPointModel);
        when(config.getRms()).thenReturn(rmsUri);
        when(restTemplate.getRestTemplate()).thenReturn(mockRestTemplate);
        when(mockRestTemplate.exchange(rmsUri, HttpMethod.POST, expectedEntity, SingleDigitDspDataOutput.class)).thenReturn(expectedResponse);

        // Act
        BigDecimal actualResult = serviceToTest.computeRMS(incomingSensorDataPointModel);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(expectedResult.doubleValue(), actualResult.doubleValue())
        );
        verify(restTemplate, times(1)).getRestTemplate();
        verify(mockRestTemplate, times(1)).exchange(rmsUri, HttpMethod.POST, expectedEntity, SingleDigitDspDataOutput.class);
    }

    @Test
    @DisplayName("should make service call to dsp crest endpoint and return data")
    void test_computeCrest_good_response() {
        // Arrange
        SensorDataPointModel incomingSensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        DspDataInput dspDataInput = new DspDataInput().data(new ArrayList<>());
        HttpEntity<DspDataInput> expectedEntity = new HttpEntity<>(dspDataInput);
        String crestUri = "https://benxin.is.the.best.com/crest";

        BigDecimal expectedResult = new BigDecimal(2);
        ResponseEntity<SingleDigitDspDataOutput> expectedResponse = new ResponseEntity<>(new SingleDigitDspDataOutput().body(new SingleDigitResultEncapsulation().result(expectedResult)), HttpStatus.OK);


        doReturn(expectedEntity).when(serviceToTest).getRequestEntity(incomingSensorDataPointModel);
        when(config.getCrest()).thenReturn(crestUri);
        when(restTemplate.getRestTemplate()).thenReturn(mockRestTemplate);
        when(mockRestTemplate.exchange(crestUri, HttpMethod.POST, expectedEntity, SingleDigitDspDataOutput.class)).thenReturn(expectedResponse);

        // Act
        BigDecimal actualResult = serviceToTest.computeCrest(incomingSensorDataPointModel);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(expectedResult.doubleValue(), actualResult.doubleValue())
        );
        verify(restTemplate, times(1)).getRestTemplate();
        verify(mockRestTemplate, times(1)).exchange(crestUri, HttpMethod.POST, expectedEntity, SingleDigitDspDataOutput.class);
    }

    @Test
    @DisplayName("should make service call to dsp shape endpoint and return data")
    void test_computeShape_good_response() {
        // Arrange
        SensorDataPointModel incomingSensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        DspDataInput dspDataInput = new DspDataInput().data(new ArrayList<>());
        HttpEntity<DspDataInput> expectedEntity = new HttpEntity<>(dspDataInput);
        String shapeUri = "https://benxin.is.the.best.com/shape";

        BigDecimal expectedResult = new BigDecimal(2);
        ResponseEntity<SingleDigitDspDataOutput> expectedResponse = new ResponseEntity<>(new SingleDigitDspDataOutput().body(new SingleDigitResultEncapsulation().result(expectedResult)), HttpStatus.OK);


        doReturn(expectedEntity).when(serviceToTest).getRequestEntity(incomingSensorDataPointModel);
        when(config.getShape()).thenReturn(shapeUri);
        when(restTemplate.getRestTemplate()).thenReturn(mockRestTemplate);
        when(mockRestTemplate.exchange(shapeUri, HttpMethod.POST, expectedEntity, SingleDigitDspDataOutput.class)).thenReturn(expectedResponse);

        // Act
        BigDecimal actualResult = serviceToTest.computeShape(incomingSensorDataPointModel);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(expectedResult.doubleValue(), actualResult.doubleValue())
        );
        verify(restTemplate, times(1)).getRestTemplate();
        verify(mockRestTemplate, times(1)).exchange(shapeUri, HttpMethod.POST, expectedEntity, SingleDigitDspDataOutput.class);
    }

    @Test
    @DisplayName("shoud return HttpEntity")
    void test_getRequestEntity() {
        // Arrange
        List<Double> data= new ArrayList<>();
        data.add(321.90);
        data.add(123.90);
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().data(data).build();

        when(restTemplate.getHeaders()).thenReturn(new HttpHeaders());

        // Act
        HttpEntity<DspDataInput> actualEntity = serviceToTest.getRequestEntity(sensorDataPointModel);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(data.get(0), Objects.requireNonNull(actualEntity.getBody()).getData().get(0).doubleValue()),
                () -> assertEquals(data.get(1), Objects.requireNonNull(actualEntity.getBody()).getData().get(1).doubleValue()),
                () -> assertNotNull(actualEntity.getHeaders())
        );
        verify(restTemplate, times(1)).getHeaders();
    }
}
