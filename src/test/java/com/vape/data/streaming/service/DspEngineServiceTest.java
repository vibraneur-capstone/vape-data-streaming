package com.vape.data.streaming.service;

import com.vape.data.streaming.config.DspEngineConfig;
import com.vape.data.streaming.config.DspEngineRestTemplate;
import com.vape.data.streaming.model.DspTopic;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.dsp.integration.swagger.v1.model.ComplexNumberResultEncapsulation;
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
import java.util.Arrays;
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
    void test_compute_time_domain_good_response() {
        // Arrange
        SensorDataPointModel incomingSensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        DspDataInput dspDataInput = new DspDataInput().data(new ArrayList<>());
        HttpEntity<DspDataInput> expectedEntity = new HttpEntity<>(dspDataInput);
        String kurtisisUri = "https://benxin.is.the.best.com/kurtosis";

        BigDecimal expectedResult = new BigDecimal(1.3);
        ResponseEntity<SingleDigitDspDataOutput> expectedResponse = new ResponseEntity<>(new SingleDigitDspDataOutput().body(new SingleDigitResultEncapsulation().result(expectedResult)), HttpStatus.OK);

        doReturn(expectedEntity).when(serviceToTest).getRequestEntity(incomingSensorDataPointModel);
        when(config.getUriByDspTopic(DspTopic.KURTOSIS)).thenReturn(kurtisisUri);
        when(restTemplate.getRestTemplate()).thenReturn(mockRestTemplate);
        when(mockRestTemplate.exchange(kurtisisUri, HttpMethod.POST, expectedEntity, SingleDigitDspDataOutput.class)).thenReturn(expectedResponse);

        // Act
        Double actualResult = serviceToTest.computeTimeDomain(DspTopic.KURTOSIS, incomingSensorDataPointModel);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(expectedResult.doubleValue(), actualResult)
        );

        verify(config, times(1)).getUriByDspTopic(DspTopic.KURTOSIS);
        verify(restTemplate, times(1)).getRestTemplate();
        verify(mockRestTemplate, times(1)).exchange(kurtisisUri, HttpMethod.POST, expectedEntity, SingleDigitDspDataOutput.class);
    }

    @Test
    @DisplayName("should make service call to dsp fft and return parsed data")
    void test_compute_freq_domain_good_response() {
        // Arrange
        SensorDataPointModel incomingSensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        HttpEntity<DspDataInput> expectedEntity = new HttpEntity<>(new DspDataInput().data(new ArrayList<>()));
        String fftUri = "benxin.is.the.best.com/fft";
        List<String> expectedFft = new ArrayList<>(Arrays.asList("2.3", "32.22223321431411322312", "2"));
        ResponseEntity<ComplexNumberResultEncapsulation> expectedResponse = new ResponseEntity<>(new ComplexNumberResultEncapsulation().result(expectedFft), HttpStatus.OK);

        doReturn(expectedEntity).when(serviceToTest).getRequestEntity(incomingSensorDataPointModel);
        when(config.getUriByDspTopic(DspTopic.FFT)).thenReturn(fftUri);
        when(restTemplate.getRestTemplate()).thenReturn(mockRestTemplate);
        when(mockRestTemplate.exchange(fftUri, HttpMethod.POST, expectedEntity, ComplexNumberResultEncapsulation.class)).thenReturn(expectedResponse);

        // Act
        List<Double> actualResult = serviceToTest.computeFreqDomain(DspTopic.FFT, incomingSensorDataPointModel);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(expectedFft.size(), actualResult.size()),
                () -> assertEquals(Double.valueOf(expectedFft.get(0)), actualResult.get(0)),
                () -> assertEquals(Double.valueOf(expectedFft.get(1)), actualResult.get(1)),
                () -> assertEquals(Double.valueOf(expectedFft.get(2)), actualResult.get(2))
        );

        verify(config, times(1)).getUriByDspTopic(DspTopic.FFT);
        verify(restTemplate, times(1)).getRestTemplate();
        verify(mockRestTemplate, times(1)).exchange(fftUri, HttpMethod.POST, expectedEntity, ComplexNumberResultEncapsulation.class);
    }

    @Test
    @DisplayName("should return null if HTTP status is not OK")
    void test_bad_response_time_domain() {
        // Arrange
        SensorDataPointModel incomingSensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        DspDataInput dspDataInput = new DspDataInput().data(new ArrayList<>());
        HttpEntity<DspDataInput> expectedEntity = new HttpEntity<>(dspDataInput);
        String shapeUri = "https://benxin.is.the.best.com/shape";

        BigDecimal expectedResult = null;
        ResponseEntity<SingleDigitDspDataOutput> expectedResponse = new ResponseEntity<>(new SingleDigitDspDataOutput().body(new SingleDigitResultEncapsulation().result(expectedResult)), HttpStatus.INTERNAL_SERVER_ERROR);


        doReturn(expectedEntity).when(serviceToTest).getRequestEntity(incomingSensorDataPointModel);
        when(config.getUriByDspTopic(DspTopic.SHAPE)).thenReturn(shapeUri);
        when(restTemplate.getRestTemplate()).thenReturn(mockRestTemplate);
        when(mockRestTemplate.exchange(shapeUri, HttpMethod.POST, expectedEntity, SingleDigitDspDataOutput.class)).thenReturn(expectedResponse);

        // Act
        Double actualResult = serviceToTest.computeTimeDomain(DspTopic.SHAPE, incomingSensorDataPointModel);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals((Object) null, actualResult)
        );
        verify(config, times(1)).getUriByDspTopic(DspTopic.SHAPE);
        verify(restTemplate, times(1)).getRestTemplate();
        verify(mockRestTemplate, times(1)).exchange(shapeUri, HttpMethod.POST, expectedEntity, SingleDigitDspDataOutput.class);
    }

    @Test
    @DisplayName("should return null if HTTP status is not OK")
    void test_bad_response_freq_domain() {
        // Arrange
        SensorDataPointModel incomingSensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        HttpEntity<DspDataInput> expectedEntity = new HttpEntity<>(new DspDataInput().data(new ArrayList<>()));
        String fftUri = "https://benxin.is.the.best.com/fft";

        BigDecimal expectedResult = null;
        ResponseEntity<ComplexNumberResultEncapsulation> expectedResponse = new ResponseEntity<>(new ComplexNumberResultEncapsulation().result(new ArrayList<>()), HttpStatus.INTERNAL_SERVER_ERROR);

        doReturn(expectedEntity).when(serviceToTest).getRequestEntity(incomingSensorDataPointModel);
        when(config.getUriByDspTopic(DspTopic.FFT)).thenReturn(fftUri);
        when(restTemplate.getRestTemplate()).thenReturn(mockRestTemplate);
        when(mockRestTemplate.exchange(fftUri, HttpMethod.POST, expectedEntity, ComplexNumberResultEncapsulation.class)).thenReturn(expectedResponse);

        // Act
        List<Double> actualResult = serviceToTest.computeFreqDomain(DspTopic.FFT, incomingSensorDataPointModel);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals((Object) null, actualResult)
        );
        verify(config, times(1)).getUriByDspTopic(DspTopic.FFT);
        verify(restTemplate, times(1)).getRestTemplate();
        verify(mockRestTemplate, times(1)).exchange(fftUri, HttpMethod.POST, expectedEntity, ComplexNumberResultEncapsulation.class);
    }


    @Test
    @DisplayName("shoud return HttpEntity")
    void test_getRequestEntity() {
        // Arrange
        List<Double> data = new ArrayList<>();
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
