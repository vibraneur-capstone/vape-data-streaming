package com.vape.data.streaming.controller;

import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.service.DataPointProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SensorDataInputControllerTest {

    @Mock
    private DataPointProducer producer;

    @InjectMocks
    private SensorDataInputController controllerToTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("should publish sensor data and persist in mongo")
    void test_controller() {
        // Arrange
        SensorDataPointModel sensorDataInput = SensorDataPointModel.builder().data(new ArrayList<>()).build();

        // Act
        ResponseEntity<SensorDataPointModel> actualResponse = controllerToTest.testProducer(sensorDataInput);

        // Assert
        verify(producer, times(1)).publishSensorData(sensorDataInput);

        assertAll("ensure ok",
                () -> assertEquals(HttpStatus.OK, actualResponse.getStatusCode()),
                () -> assertEquals(sensorDataInput, actualResponse.getBody())
                );
    }
}
