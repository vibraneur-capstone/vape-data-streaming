package com.vape.data.streaming.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vape.data.streaming.model.KurtosisDataPointModel;
import com.vape.data.streaming.model.RMSDataPointModel;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.utility.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DataPointConsumerTest {

    @Spy
    @InjectMocks
    private DataPointConsumer serviceToTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private JsonMapper mapper;
    @Mock
    private DspEngineService dspEngineService;

    @Mock
    private DataPointProducer dataPointProducer;

    @Test
    @DisplayName("should compute RMS and publish to Kafka")
    void test_rms() throws JsonProcessingException, IOException {
        // Arrange
        String kafkaMessage = "{\"id\":\"567\",\"sensorId\":\"123\",\"sensorHubId\":null,\"timestamp\":null,\"data\":[123,321]}";
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().id("567").sensorId("123").build();
        RMSDataPointModel constructedModel = RMSDataPointModel.builder().sensorDataPointId("567").data(1.0).build();
        Double computedRMS = 1.3;

        when(mapper.toObject(kafkaMessage, SensorDataPointModel.class)).thenReturn(sensorDataPointModel);
        doReturn(constructedModel).when(serviceToTest).constructRMSDataPointModel(sensorDataPointModel, computedRMS);
        when(dspEngineService.computeRMS(sensorDataPointModel)).thenReturn(new BigDecimal(computedRMS));

        // Act
        serviceToTest.computeRMS(kafkaMessage);

        // Assert
        verify(mapper, times(1)).toObject(eq(kafkaMessage), eq(SensorDataPointModel.class));
        verify(dspEngineService, times(1)).computeRMS(sensorDataPointModel);
        verify(serviceToTest, times(1)).constructRMSDataPointModel(sensorDataPointModel, computedRMS);
        verify(dataPointProducer, times(1)).publishRMS(constructedModel);
    }

    @Test
    @DisplayName("should compute kurtosis and publish to Kafka")
    void test_kurtosis() throws IOException {
        // Arrange
        String kafkaMessage = "{\"id\":\"567\",\"sensorId\":\"123\",\"sensorHubId\":null,\"timestamp\":null,\"data\":[123,321]}";
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().id("567").sensorId("123").build();
        KurtosisDataPointModel constructedModel = KurtosisDataPointModel.builder().sensorDataPointId("567").data(1.0).build();
        Double computedKurtosis = 1.3;

        when(mapper.toObject(kafkaMessage, SensorDataPointModel.class)).thenReturn(sensorDataPointModel);
        doReturn(constructedModel).when(serviceToTest).constructKurtosisDataPointModel(sensorDataPointModel, computedKurtosis);
        when(dspEngineService.computeKurtosis(sensorDataPointModel)).thenReturn(new BigDecimal(1.3));

        // Act
        serviceToTest.computeKurtosis(kafkaMessage);

        // Assert
        verify(mapper, times(1)).toObject(eq(kafkaMessage), eq(SensorDataPointModel.class));
        verify(dspEngineService, times(1)).computeKurtosis(sensorDataPointModel);
        verify(serviceToTest, times(1)).constructKurtosisDataPointModel(sensorDataPointModel, computedKurtosis);
        verify(dataPointProducer, times(1)).publishKurtosis(constructedModel);
    }

    @Test
    @DisplayName("shoud construct KurtosisDataPointModel")
    void test_KurtosisDataPointModel() {
        // Arrange
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().id("567").build();
        Double computedData = 123.0;

        // Act
        KurtosisDataPointModel actualModel = serviceToTest.constructKurtosisDataPointModel(sensorDataPointModel, computedData);

        // Assert
        assertAll("ensure proper mapping",
                () -> assertNotNull(actualModel.getTimestamp()),
                () -> assertEquals(computedData, actualModel.getData()),
                () -> assertEquals("567", actualModel.getSensorDataPointId())
                );
    }

    @Test
    @DisplayName("shoud construct RMSDataPointModel")
    void test_RMSDataPointModel() {
        // Arrange
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().id("123").build();
        Double computedData = 321.0;

        // Act
        RMSDataPointModel actualModel = serviceToTest.constructRMSDataPointModel(sensorDataPointModel, computedData);

        // Assert
        assertAll("ensure proper mapping",
                () -> assertNotNull(actualModel.getTimestamp()),
                () -> assertEquals(computedData, actualModel.getData()),
                () -> assertEquals("123", actualModel.getSensorDataPointId())
        );
    }
}
