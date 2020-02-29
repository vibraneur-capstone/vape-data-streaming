package com.vape.data.streaming.service;

import com.vape.data.streaming.model.*;
import com.vape.data.streaming.repository.*;
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
    private SensorDataPointModelRepository sensorDataPointModelRepository;

    @Mock
    private DspDataPointModelRepository dspDataPointModelRepository;

    @Test
    @DisplayName("should store raw data in mongo then publish stored data to topic: SENSOR")
    void test_publishSensorData_method() {
        // Arrange
        SensorDataPointModel incomingSensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        SensorDataPointModel savedSensorDataPointModel = SensorDataPointModel.builder().id("123").sensorId("123").build();
        String expectedMsg = "test message";

        when(sensorDataPointModelRepository.save(incomingSensorDataPointModel)).thenReturn(savedSensorDataPointModel);
        when(mapper.toJson(savedSensorDataPointModel)).thenReturn(expectedMsg);

        // Act
        SensorDataPointModel actualSensorDataPointModel = serviceToTest.publishSensorData(incomingSensorDataPointModel);

        // Assert
        assertAll("ensure OK",
                () -> assertNotNull(incomingSensorDataPointModel.getTimestamp()),
                () -> assertEquals(savedSensorDataPointModel, actualSensorDataPointModel)
        );
        verify(mapper, times(1)).toJson(savedSensorDataPointModel);
        verify(sensorDataPointModelRepository, times(1)).save(incomingSensorDataPointModel);
        verify(kafkaTemplate, times(1)).send(eq("SENSOR"), eq(expectedMsg));
    }

    @Test
    @DisplayName("should store dsp data in mongo then publish stored data to topic: DSP")
    void test_publishDspData_method() {
        // Arrange
        DspDataPointModel dspDataPointModel = DspDataPointModel.builder().crest(2.3).sensorDataPointId("test id").build();
        DspDataPointModel savedDspDataPointModel = DspDataPointModel.builder().id("id").crest(2.3).sensorDataPointId("test id").build();

        String expectedMsg = "test message";

        when(dspDataPointModelRepository.save(dspDataPointModel)).thenReturn(savedDspDataPointModel);
        when(mapper.toJson(savedDspDataPointModel)).thenReturn(expectedMsg);

        // Act
        DspDataPointModel actualSavedDspDataPointModel = serviceToTest.publishDspData(dspDataPointModel);

        // Assert
        assertAll("ensure OK",
                () -> assertNotNull(dspDataPointModel.getTimestamp()),
                () -> assertEquals(savedDspDataPointModel, actualSavedDspDataPointModel)
        );
        verify(mapper, times(1)).toJson(savedDspDataPointModel);
        verify(dspDataPointModelRepository, times(1)).save(dspDataPointModel);
        verify(kafkaTemplate, times(1)).send(eq(KafkaTopic.DSP.toString()), eq(expectedMsg));
    }
}
