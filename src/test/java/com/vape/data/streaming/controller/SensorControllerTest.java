package com.vape.data.streaming.controller;

import com.vape.data.streaming.mapper.SensorDataPointModelMapper;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.service.DataPointProducer;
import com.vape.data.streaming.swagger.v1.model.SensorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SensorControllerTest {

    @Mock
    private DataPointProducer producer;

    @InjectMocks
    private SensorController controllerToTest;

    @Mock
    private SensorDataPointModelMapper mapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("should publish sensor data and persist in mongo")
    void test_sensorPost() {
        // Arrange
        String sensorId = "test id";
        SensorData sensorData = new SensorData().sensorId(sensorId).data(new ArrayList<>());
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().sensorId(sensorId).data(new ArrayList<>()).build();
        when(mapper.toSensorDataPointModel(sensorData)).thenReturn(sensorDataPointModel);

        // Act
        ResponseEntity<SensorData> actualResponse = controllerToTest.sensorPost(sensorData, sensorId);

        // Assert
        verify(producer, times(1)).publishSensorData(sensorDataPointModel);

        assertAll("ensure ok",
                () -> assertEquals(HttpStatus.OK, actualResponse.getStatusCode()),
                () -> assertEquals(sensorData, actualResponse.getBody())
                );
    }

    @Test
    @DisplayName("should return 400 if sensorId in body is null")
    void test_bad_request() {
        // Arrange
        SensorData sensorData = new SensorData().sensorId(null).data(new ArrayList<>());

        // Act
        ResponseEntity<SensorData> actualResponse = controllerToTest.sensorPost(sensorData, "sensorId");

        // Assert
        verify(producer, times(0)).publishSensorData(any());

        assertAll("ensure ok",
                () -> assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode()),
                () -> assertEquals(sensorData, actualResponse.getBody())
        );
    }

    @Test
    @DisplayName("should return 400 if sensorId in body is equal to query")
    void test_bad_request_case_two() {
        // Arrange
        SensorData sensorData = new SensorData().sensorId("test").data(new ArrayList<>());

        // Act
        ResponseEntity<SensorData> actualResponse = controllerToTest.sensorPost(sensorData, "sensorId");

        // Assert
        verify(producer, times(0)).publishSensorData(any());

        assertAll("ensure ok",
                () -> assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode()),
                () -> assertEquals(sensorData, actualResponse.getBody())
        );
    }
}
