package com.vape.data.streaming.controller;

import com.vape.data.streaming.mapper.DspDataPointModelMapper;
import com.vape.data.streaming.model.DspDataPointModel;
import com.vape.data.streaming.service.DataQueryService;
import com.vape.data.streaming.swagger.v1.model.DataStatus;
import com.vape.data.streaming.swagger.v1.model.DspDataList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DspDataControllerTest {

    @InjectMocks
    DspDataController controllerToTest;

    @Mock
    private DataQueryService dataQueryService;

    @Mock
    private DspDataPointModelMapper dspDataPointModelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("should get data from dsp collection and map to DspDataList")
    public void test_controller() {
        // Arrange
        String sensorId = "test id";
        Integer from = 1583119800;
        Integer to = 1583292600;
        List<DspDataPointModel> mockDspDataPointModelList = new ArrayList<>();
        mockDspDataPointModelList.add(DspDataPointModel.builder().crest(1.1).build());
        DspDataList expected = new DspDataList().addCrestItem(new BigDecimal(1.1));

        when(dataQueryService.getDspDataBySensorAndDateRange(sensorId, from, to)).thenReturn(mockDspDataPointModelList);
        when(dspDataPointModelMapper.toDspDataList(mockDspDataPointModelList)).thenReturn(expected);

        // Act
        ResponseEntity<DspDataList> actual = controllerToTest.dspDataGet(sensorId, from, to);

        // Assert
        assertAll("Ensure ok",
                () -> assertNotNull(actual.getBody()),
                () -> assertEquals(200, actual.getStatusCodeValue()),
                () -> assertEquals(expected.getCrest().size(), actual.getBody().getCrest().size()),
                () -> assertEquals(1.1, actual.getBody().getCrest().get(0).doubleValue()),
                () -> assertEquals(sensorId, actual.getBody().getSensorId()),
                () -> assertEquals(from, actual.getBody().getFrom()),
                () -> assertEquals(to, actual.getBody().getTo())
        );

        verify(dataQueryService, times(1)).getDspDataBySensorAndDateRange(sensorId, from, to);
        verify(dspDataPointModelMapper, times(1)).toDspDataList(mockDspDataPointModelList);
    }

    @Test
    @DisplayName("should return")
    public void test_dspDataStatusGet() {
        // Arrange
        Integer from = 1583119800;
        Integer to = 1583292600;
        String sensorId = "test id";

        // Act
        ResponseEntity<DataStatus> actual = controllerToTest.dspDataStatusGet(sensorId, from, to);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(200, actual.getStatusCodeValue()),
                () -> assertNotNull(actual.getBody())
        );
    }
}
