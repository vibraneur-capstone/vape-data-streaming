package com.vape.data.streaming.controller;

import com.vape.data.streaming.mapper.SensorDataPointModelMapper;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.service.DataPointProducer;
import com.vape.data.streaming.service.DataQueryService;
import com.vape.data.streaming.swagger.v1.model.SensorData;
import com.vape.data.streaming.swagger.v1.model.SensorDataList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SensorDataControllerTest {

    @Mock
    private DataPointProducer producer;

    @InjectMocks
    private SensorDataController controllerToTest;

    @Mock
    private SensorDataPointModelMapper mapper;

    @Mock
    private DataQueryService dataQueryService;


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

    @Test
    @DisplayName("should return")
    public void test_dspDataStatusGet() {
        // Arrange
        Integer from = 1583119800;
        Integer to = 1583292600;
        String sensorId = "test id";
        List<SensorDataPointModel> mockModelList = getMockSensorDataPointModelList();

        when(dataQueryService.getSensorDataByIdAndDateRange(sensorId, from, to)).thenReturn(mockModelList);
        when(mapper.toSensorDataList(mockModelList)).thenReturn(getMockSensorDataList());

        // Act
        ResponseEntity<SensorDataList> actual = controllerToTest.sensorGet(sensorId, from, to);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(200, actual.getStatusCodeValue()),
                () -> assertNotNull(actual.getBody()),
                () -> assertEquals(2, actual.getBody().getData().size()),
                () -> assertEquals(1.2, actual.getBody().getData().get(0).doubleValue()),
                () -> assertEquals(2.2, actual.getBody().getData().get(1).doubleValue()),
                () -> assertEquals(from, actual.getBody().getFrom()),
                () -> assertEquals(to, actual.getBody().getTo()),
                () -> assertEquals(sensorId, actual.getBody().getSensorId())
        );

        verify(dataQueryService, times(1)).getSensorDataByIdAndDateRange(sensorId, from, to);
        verify(mapper, times(1)).toSensorDataList(mockModelList);
    }

    private SensorDataList getMockSensorDataList() {
        SensorDataList sensorDataList = new SensorDataList();
        List<BigDecimal> data = new ArrayList<>();
        data.add(new BigDecimal(1.2));
        data.add(new BigDecimal(2.2));
        sensorDataList.setData(data);
        return sensorDataList;
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
