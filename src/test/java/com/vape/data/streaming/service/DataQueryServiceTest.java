package com.vape.data.streaming.service;

import com.vape.data.streaming.model.DspDataPointModel;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.repository.DspDataPointModelRepository;
import com.vape.data.streaming.repository.SensorDataPointModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DataQueryServiceTest {

    @InjectMocks
    private DataQueryService serviceToTest;

    @Mock
    private DspDataPointModelRepository dspDataPointModelRepository;

    @Mock
    private SensorDataPointModelRepository sensorDataPointModelRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test()
    @DisplayName("should parse date and invoke correct method from repository")
    public void test_find_data_by_id_and_date_range() {
        // Arrange
        int from = 1583162178;
        int to = 1583162178;
        String sensorId = "test id";
        String parsed_from = LocalDateTime.ofInstant(Instant.ofEpochSecond(from), ZoneId.systemDefault()).toString();
        String parsed_to = LocalDateTime.ofInstant(Instant.ofEpochSecond(to), ZoneId.systemDefault()).toString();

        when(dspDataPointModelRepository.findDspDataBySensorAndDateRange(sensorId, parsed_from, parsed_to)).thenReturn(new ArrayList<>());

        // Act
        List<DspDataPointModel> actualList = serviceToTest.getDspDataBySensorAndDateRange(sensorId, from, to);

        // Assert

        assertAll("ensure ok",
                ()-> assertEquals(new ArrayList<>(), actualList)
        );

        verify(dspDataPointModelRepository, times(1)).findDspDataBySensorAndDateRange(sensorId, parsed_from, parsed_to);
    }

    @Test()
    @DisplayName("should parse date and invoke correct method from repository")
    public void test_find_sensor_data_by_id_and_date_range() {
        // Arrange
        int from = 1583162178;
        int to = 1583162178;
        String sensorId = "test id";
        String parsed_from = LocalDateTime.ofInstant(Instant.ofEpochSecond(from), ZoneId.systemDefault()).toString();
        String parsed_to = LocalDateTime.ofInstant(Instant.ofEpochSecond(to), ZoneId.systemDefault()).toString();

        when(sensorDataPointModelRepository.findSensorDataByIdAndDateRange(sensorId, parsed_from, parsed_to)).thenReturn(new ArrayList<>());

        // Act
        List<SensorDataPointModel> actualList = serviceToTest.getSensorDataByIdAndDateRange(sensorId, from, to);

        // Assert

        assertAll("ensure ok",
                ()-> assertEquals(new ArrayList<>(), actualList)
        );

        verify(sensorDataPointModelRepository, times(1)).findSensorDataByIdAndDateRange(sensorId, parsed_from, parsed_to);
    }
}
