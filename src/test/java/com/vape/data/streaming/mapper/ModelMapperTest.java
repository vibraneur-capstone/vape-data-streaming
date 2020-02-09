package com.vape.data.streaming.mapper;

import com.vape.data.streaming.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelMapperTest {

    @InjectMocks
    ModelMapper mapperToTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("shoud construct KurtosisDataPointModel")
    void test_KurtosisDataPointModel() {
        // Arrange
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().id("567").build();
        BigDecimal computedData = new BigDecimal(123);

        // Act
        KurtosisDataPointModel actualModel = mapperToTest.constructKurtosisDataPointModel(sensorDataPointModel, computedData);

        // Assert
        assertAll("ensure proper mapping",
                () -> assertNotNull(actualModel.getTimestamp()),
                () -> assertEquals(computedData.doubleValue(), actualModel.getData()),
                () -> assertEquals("567", actualModel.getSensorDataPointId())
        );
    }

    @Test
    @DisplayName("shoud construct RMSDataPointModel")
    void test_RMSDataPointModel() {
        // Arrange
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().id("123").build();
        BigDecimal computedData = new BigDecimal(321);

        // Act
        RMSDataPointModel actualModel = mapperToTest.constructRMSDataPointModel(sensorDataPointModel, computedData);

        // Assert
        assertAll("ensure proper mapping",
                () -> assertNotNull(actualModel.getTimestamp()),
                () -> assertEquals(computedData.doubleValue(), actualModel.getData()),
                () -> assertEquals("123", actualModel.getSensorDataPointId())
        );
    }

    @Test
    @DisplayName("shoud construct CrestDataPointModel")
    void test_CrestDataPointModel() {
        // Arrange
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().id("567").build();
        BigDecimal computedData = new BigDecimal(123);

        // Act
        CrestDataPointModel actualModel = mapperToTest.constructCrestDataPointModel(sensorDataPointModel, computedData);

        // Assert
        assertAll("ensure proper mapping",
                () -> assertNotNull(actualModel.getTimestamp()),
                () -> assertEquals(computedData.doubleValue(), actualModel.getData()),
                () -> assertEquals("567", actualModel.getSensorDataPointId())
        );
    }

    @Test
    @DisplayName("shoud construct ShapeDataPointModel")
    void test_ShapeDataPointModel() {
        // Arrange
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().id("123").build();
        BigDecimal computedData = new BigDecimal(321);

        // Act
        ShapeDataPointModel actualModel = mapperToTest.constructShapeDataPointModel(sensorDataPointModel, computedData);

        // Assert
        assertAll("ensure proper mapping",
                () -> assertNotNull(actualModel.getTimestamp()),
                () -> assertEquals(computedData.doubleValue(), actualModel.getData()),
                () -> assertEquals("123", actualModel.getSensorDataPointId())
        );
    }
}
