package com.vape.data.streaming.mapper;

import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.swagger.v1.model.SensorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DtoMapperTest {

    @InjectMocks
    private DtoMapper mapperToTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("test mapping from SensorData to SensorDataPointModel")
    public void test_mapper() {
        // Arrange
        List<BigDecimal> mockData = new ArrayList<>();
        mockData.add(new BigDecimal(1.2));
        mockData.add(new BigDecimal(1.89));
        SensorData inputData = new SensorData().data(mockData).sensorId("test");

        // Act
        SensorDataPointModel dataPointModel = mapperToTest.toSensorDataPointModel(inputData);

        // Assert
        assertAll("Ensure ok",
                () -> assertEquals(dataPointModel.getSensorId(), "test"),
                () -> assertEquals(dataPointModel.getData().get(0), mockData.get(0).doubleValue()),
                () -> assertEquals(dataPointModel.getData().get(1), mockData.get(1).doubleValue())
        );
    }
}
