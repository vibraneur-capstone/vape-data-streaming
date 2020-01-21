package com.vape.data.streaming.service;

import com.vape.data.streaming.model.FFTDataPointModel;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.repository.SensorDataPointRepository;
import com.vape.data.streaming.utility.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DataPointProducerTest {
    @InjectMocks
    private DataPointProducer serviceToTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private JsonMapper mapper;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private SensorDataPointRepository sensorDataPointRepository;

    @Test
    @DisplayName("should store raw data in mongo then publish stored data to topic: SENSOR")
    void test_publishSensorData_method() {
        // Arrange
        SensorDataPointModel incomingSensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        SensorDataPointModel mongoSensorDataPointModel = SensorDataPointModel.builder().id("123").sensorId("123").build();
        String expectedMsg = "test message";

        when(sensorDataPointRepository.save(incomingSensorDataPointModel)).thenReturn(mongoSensorDataPointModel);
        when(mapper.toJson(mongoSensorDataPointModel)).thenReturn(expectedMsg);

        // Act
        serviceToTest.publishSensorData(incomingSensorDataPointModel);

        // Assert
        assertAll("ensure OK",
                () -> assertNotNull(incomingSensorDataPointModel.getTimestamp())
        );
        verify(sensorDataPointRepository, times(1)).save(incomingSensorDataPointModel);
        verify(kafkaTemplate, times(1)).send(eq("SENSOR"), eq(expectedMsg));
    }

    @Test
    @DisplayName("should publish FFT data")
    void test_publish_publishFFT_method() {
        // Arrange
        FFTDataPointModel fftDataPointModel = FFTDataPointModel.builder().id("test id").build();
        String expectedMsg = "test message";

        when(mapper.toJson(fftDataPointModel)).thenReturn(expectedMsg);

        // Act
        serviceToTest.publishFFT(fftDataPointModel);

        // Assert
        verify(kafkaTemplate, times(1)).send(eq("FFT"), eq(expectedMsg));
    }
}
