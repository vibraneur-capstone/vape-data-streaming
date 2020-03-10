package com.vape.data.streaming.service;

import com.vape.data.streaming.model.DspDataPointModel;
import com.vape.data.streaming.model.KafkaTopic;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.repository.DspDataPointModelRepository;
import com.vape.data.streaming.repository.SensorDataPointModelRepository;
import com.vape.data.streaming.swagger.v1.model.Status;
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

import static org.junit.jupiter.api.Assertions.*;
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
                () -> assertEquals(new ArrayList<>(), actualList)
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
                () -> assertEquals(new ArrayList<>(), actualList)
        );

        verify(sensorDataPointModelRepository, times(1)).findSensorDataByIdAndDateRange(sensorId, parsed_from, parsed_to);
    }

    @Test
    @DisplayName("should get Status of a type")
    void test_get_status_with_dsp() {
        // Arrange
        String mockSensorId = "123";
        KafkaTopic topic = KafkaTopic.DSP;
        List<DspDataPointModel> mockList = new ArrayList<>();
        mockList.add(DspDataPointModel.builder().timestamp("2020/07/07").build());
        mockList.add(DspDataPointModel.builder().timestamp("2020/08/07").build());

        when(dspDataPointModelRepository.findAllTimestampsBySensorId(eq(mockSensorId))).thenReturn(mockList);

        // Act
        Status actual = serviceToTest.getDspOrSensorDataStats(mockSensorId, topic);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(2, actual.getCount()),
                () -> assertEquals("2020/07/07", actual.getEarliestTimestamp()),
                () -> assertEquals("2020/08/07", actual.getLatestTimestamp())
        );

        verify(dspDataPointModelRepository, times(1)).findAllTimestampsBySensorId(eq(mockSensorId));
        verify(sensorDataPointModelRepository, times(0)).findAllTimestampsBySensorId(any());
    }

    @Test
    @DisplayName("should get Status of dsp but null is returned by repository")
    void test_get_status_with_dsp_null_list_returned() {
        // Arrange
        String mockSensorId = "123";
        KafkaTopic topic = KafkaTopic.DSP;

        when(dspDataPointModelRepository.findAllTimestampsBySensorId(eq(mockSensorId))).thenReturn(null);

        // Act
        Status actual = serviceToTest.getDspOrSensorDataStats(mockSensorId, topic);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(0, actual.getCount()),
                () -> assertNull(actual.getEarliestTimestamp()),
                () -> assertNull(actual.getLatestTimestamp())
        );

        verify(dspDataPointModelRepository, times(1)).findAllTimestampsBySensorId(eq(mockSensorId));
        verify(sensorDataPointModelRepository, times(0)).findAllTimestampsBySensorId(any());
    }

    @Test
    @DisplayName("should get Status of dsp but empty list is returned by repository")
    void test_get_status_with_dsp_empty_list_returned() {
        // Arrange
        String mockSensorId = "123";
        KafkaTopic topic = KafkaTopic.DSP;

        when(dspDataPointModelRepository.findAllTimestampsBySensorId(eq(mockSensorId))).thenReturn(new ArrayList<>());

        // Act
        Status actual = serviceToTest.getDspOrSensorDataStats(mockSensorId, topic);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(0, actual.getCount()),
                () -> assertNull(actual.getEarliestTimestamp()),
                () -> assertNull(actual.getLatestTimestamp())
        );

        verify(dspDataPointModelRepository, times(1)).findAllTimestampsBySensorId(eq(mockSensorId));
        verify(sensorDataPointModelRepository, times(0)).findAllTimestampsBySensorId(any());
    }

    @Test
    @DisplayName("should get Status of a type")
    void test_get_status_with_sensor() {
        // Arrange
        String mockSensorId = "123";
        KafkaTopic topic = KafkaTopic.SENSOR;
        List<SensorDataPointModel> mockList = new ArrayList<>();
        mockList.add(SensorDataPointModel.builder().timestamp("2020/07/07").build());

        when(sensorDataPointModelRepository.findAllTimestampsBySensorId(eq(mockSensorId))).thenReturn(mockList);

        // Act
        Status actual = serviceToTest.getDspOrSensorDataStats(mockSensorId, topic);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(1, actual.getCount()),
                () -> assertEquals("2020/07/07", actual.getEarliestTimestamp()),
                () -> assertEquals("2020/07/07", actual.getLatestTimestamp())
        );

        verify(dspDataPointModelRepository, times(0)).findAllTimestampsBySensorId(any());
        verify(sensorDataPointModelRepository, times(1)).findAllTimestampsBySensorId(eq(mockSensorId));
    }

    @Test
    @DisplayName("should get Status of sensor data but null is returned by repository")
    void test_get_status_with_sensor_null_list_returned() {
        // Arrange
        String mockSensorId = "123";
        KafkaTopic topic = KafkaTopic.SENSOR;

        when(sensorDataPointModelRepository.findAllTimestampsBySensorId(eq(mockSensorId))).thenReturn(null);

        // Act
        Status actual = serviceToTest.getDspOrSensorDataStats(mockSensorId, topic);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(0, actual.getCount()),
                () -> assertNull(actual.getEarliestTimestamp()),
                () -> assertNull(actual.getLatestTimestamp())
        );

        verify(dspDataPointModelRepository, times(0)).findAllTimestampsBySensorId(any());
        verify(sensorDataPointModelRepository, times(1)).findAllTimestampsBySensorId(eq(mockSensorId));
    }

    @Test
    @DisplayName("should get Status of sensor data but empty list is returned by repository")
    void test_get_status_with_sensor_empty_list_returned() {
        // Arrange
        String mockSensorId = "123";
        KafkaTopic topic = KafkaTopic.SENSOR;

        when(sensorDataPointModelRepository.findAllTimestampsBySensorId(eq(mockSensorId))).thenReturn(new ArrayList<>());

        // Act
        Status actual = serviceToTest.getDspOrSensorDataStats(mockSensorId, topic);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(0, actual.getCount()),
                () -> assertNull(actual.getEarliestTimestamp()),
                () -> assertNull(actual.getLatestTimestamp())
        );

        verify(dspDataPointModelRepository, times(0)).findAllTimestampsBySensorId(any());
        verify(sensorDataPointModelRepository, times(1)).findAllTimestampsBySensorId(eq(mockSensorId));
    }

    @Test
    @DisplayName("should new null safe if wrong type is provided for getting status")
    void test_get_status_with_wrong_type_provided() {
        // Arrange
        String mockSensorId = "123";
        KafkaTopic wrongType = KafkaTopic.FFT;

        // Act
        Status actual = serviceToTest.getDspOrSensorDataStats(mockSensorId, wrongType);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(0, actual.getCount()),
                () -> assertNull(actual.getEarliestTimestamp()),
                () -> assertNull(actual.getLatestTimestamp())
        );

        verify(dspDataPointModelRepository, times(0)).findAllTimestampsBySensorId(any());
        verify(sensorDataPointModelRepository, times(0)).findAllTimestampsBySensorId(any());
    }
}
