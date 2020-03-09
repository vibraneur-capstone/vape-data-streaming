package com.vape.data.streaming.mapper;

import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.swagger.v1.model.SensorData;
import com.vape.data.streaming.swagger.v1.model.SensorDataList;
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

public class SensorDataPointModelMapperTest {

    @InjectMocks
    private SensorDataPointModelMapper mapperToTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("test mapping from list of SensorDataPointModel to SensorDataList")
    public void test_mapper_sensorDataList() {
        // Arrange
        List<SensorDataPointModel> sensorDataPointModelList = getMockSensorDataPointModelList();

        // Act
        SensorDataList sensorDataList = mapperToTest.toSensorDataList(sensorDataPointModelList);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(4, sensorDataList.getData().size()),
                () -> assertEquals(1.2, sensorDataList.getData().get(0).doubleValue()),
                () -> assertEquals(2.2, sensorDataList.getData().get(1).doubleValue()),
                () -> assertEquals(3.2, sensorDataList.getData().get(2).doubleValue()),
                () -> assertEquals(4.2, sensorDataList.getData().get(3).doubleValue())
                );
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

    private List<SensorDataPointModel> getMockSensorDataPointModelList() {
        List<SensorDataPointModel> sensorDataPointModelList = new ArrayList<>();
        List<Double> dataPointTwo = new ArrayList<>();
        List<Double> dataPointOne = new ArrayList<>();
        dataPointOne.add(1.2);
        dataPointOne.add(2.2);
        dataPointOne.add(3.2);
        dataPointOne.add(4.2);
        sensorDataPointModelList.add(SensorDataPointModel.builder().data(dataPointOne).build());
        sensorDataPointModelList.add(SensorDataPointModel.builder().data(dataPointTwo).build());
        return sensorDataPointModelList;
    }
}
