package com.vape.data.streaming.mapper;

import com.vape.data.streaming.model.DspDataPointModel;
import com.vape.data.streaming.swagger.v1.model.DspDataList;
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

public class DspDataPointModelMapperTest {

    @InjectMocks
    DspDataPointModelMapper mapperToTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("should return a valid DspDataList from List<DspDataPointModel>")
    public void test_toDspDataList_mapping_no_missing_data() {
        // Arrange
        List<DspDataPointModel> mockList = getDspDataPointModelList();

        // Act
        DspDataList actual = mapperToTest.toDspDataList(mockList);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(2, actual.getCount()),
                () -> assertEquals(2, actual.getCrest().size()),
                () -> assertEquals(2, actual.getKurtosis().size()),
                () -> assertEquals(2, actual.getRms().size()),
                () -> assertEquals(2, actual.getShape().size()),
                () -> assertEquals(4, actual.getFft().size()),
                () -> assertEquals(2, actual.getSensorDataPointIdList().size()),
                () -> assertEquals(new BigDecimal(2.0).doubleValue(), actual.getCrest().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(2.1).doubleValue(), actual.getCrest().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(1.0).doubleValue(), actual.getShape().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.1).doubleValue(), actual.getShape().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(3.0).doubleValue(), actual.getRms().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(3.1).doubleValue(), actual.getRms().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(4.0).doubleValue(), actual.getKurtosis().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(4.1).doubleValue(), actual.getKurtosis().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(1.2).doubleValue(), actual.getFft().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.3).doubleValue(), actual.getFft().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(2.2).doubleValue(), actual.getFft().get(2).doubleValue()),
                () -> assertEquals(new BigDecimal(2.3).doubleValue(), actual.getFft().get(3).doubleValue()),
                () -> assertEquals("test id one", actual.getSensorDataPointIdList().get(0)),
                () -> assertEquals("test id two", actual.getSensorDataPointIdList().get(1))
                );
    }

    @Test
    @DisplayName("should return a valid DspDataList from List<DspDataPointModel> skipping missing crest data")
    public void test_toDspDataList_mapping_missing_crest_data() {
        // Arrange
        List<DspDataPointModel> mockList = getDspDataPointModelList();
        mockList.get(1).setCrest(null);

        // Act
        DspDataList actual = mapperToTest.toDspDataList(mockList);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(1, actual.getCrest().size()),
                () -> assertEquals(2, actual.getKurtosis().size()),
                () -> assertEquals(2, actual.getRms().size()),
                () -> assertEquals(2, actual.getShape().size()),
                () -> assertEquals(4, actual.getFft().size()),
                () -> assertEquals(2, actual.getSensorDataPointIdList().size()),
                () -> assertEquals(new BigDecimal(2.0).doubleValue(), actual.getCrest().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.0).doubleValue(), actual.getShape().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.1).doubleValue(), actual.getShape().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(3.0).doubleValue(), actual.getRms().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(3.1).doubleValue(), actual.getRms().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(4.0).doubleValue(), actual.getKurtosis().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(4.1).doubleValue(), actual.getKurtosis().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(1.2).doubleValue(), actual.getFft().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.3).doubleValue(), actual.getFft().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(2.2).doubleValue(), actual.getFft().get(2).doubleValue()),
                () -> assertEquals(new BigDecimal(2.3).doubleValue(), actual.getFft().get(3).doubleValue()),
                () -> assertEquals("test id one", actual.getSensorDataPointIdList().get(0)),
                () -> assertEquals("test id two", actual.getSensorDataPointIdList().get(1))
        );
    }

    @Test
    @DisplayName("should return a valid DspDataList from List<DspDataPointModel> skipping missing kurtosis data")
    public void test_toDspDataList_mapping_missing_kurtosis_data() {
        // Arrange
        List<DspDataPointModel> mockList = getDspDataPointModelList();
        mockList.get(0).setKurtosis(null);

        // Act
        DspDataList actual = mapperToTest.toDspDataList(mockList);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(2, actual.getCrest().size()),
                () -> assertEquals(1, actual.getKurtosis().size()),
                () -> assertEquals(2, actual.getRms().size()),
                () -> assertEquals(2, actual.getShape().size()),
                () -> assertEquals(4, actual.getFft().size()),
                () -> assertEquals(2, actual.getSensorDataPointIdList().size()),
                () -> assertEquals(new BigDecimal(2.0).doubleValue(), actual.getCrest().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(2.1).doubleValue(), actual.getCrest().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(1.0).doubleValue(), actual.getShape().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.1).doubleValue(), actual.getShape().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(3.0).doubleValue(), actual.getRms().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(3.1).doubleValue(), actual.getRms().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(4.1).doubleValue(), actual.getKurtosis().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.2).doubleValue(), actual.getFft().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.3).doubleValue(), actual.getFft().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(2.2).doubleValue(), actual.getFft().get(2).doubleValue()),
                () -> assertEquals(new BigDecimal(2.3).doubleValue(), actual.getFft().get(3).doubleValue()),
                () -> assertEquals("test id one", actual.getSensorDataPointIdList().get(0)),
                () -> assertEquals("test id two", actual.getSensorDataPointIdList().get(1))
        );
    }

    @Test
    @DisplayName("should return a valid DspDataList from List<DspDataPointModel> skipping missing RMS data")
    public void test_toDspDataList_mapping_missing_RMS_data() {
        // Arrange
        List<DspDataPointModel> mockList = getDspDataPointModelList();
        mockList.get(1).setRms(null);

        // Act
        DspDataList actual = mapperToTest.toDspDataList(mockList);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(2, actual.getCrest().size()),
                () -> assertEquals(2, actual.getKurtosis().size()),
                () -> assertEquals(1, actual.getRms().size()),
                () -> assertEquals(2, actual.getShape().size()),
                () -> assertEquals(4, actual.getFft().size()),
                () -> assertEquals(2, actual.getSensorDataPointIdList().size()),
                () -> assertEquals(new BigDecimal(2.0).doubleValue(), actual.getCrest().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(2.1).doubleValue(), actual.getCrest().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(1.0).doubleValue(), actual.getShape().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.1).doubleValue(), actual.getShape().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(3.0).doubleValue(), actual.getRms().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(4.0).doubleValue(), actual.getKurtosis().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(4.1).doubleValue(), actual.getKurtosis().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(1.2).doubleValue(), actual.getFft().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.3).doubleValue(), actual.getFft().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(2.2).doubleValue(), actual.getFft().get(2).doubleValue()),
                () -> assertEquals(new BigDecimal(2.3).doubleValue(), actual.getFft().get(3).doubleValue()),
                () -> assertEquals("test id one", actual.getSensorDataPointIdList().get(0)),
                () -> assertEquals("test id two", actual.getSensorDataPointIdList().get(1))
        );
    }

    @Test
    @DisplayName("should return a valid DspDataList from List<DspDataPointModel> skipping missing Shape data")
    public void test_toDspDataList_mapping_missing_shape_data() {
        // Arrange
        List<DspDataPointModel> mockList = getDspDataPointModelList();
        mockList.get(1).setShape(null);

        // Act
        DspDataList actual = mapperToTest.toDspDataList(mockList);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(2, actual.getCrest().size()),
                () -> assertEquals(2, actual.getKurtosis().size()),
                () -> assertEquals(2, actual.getRms().size()),
                () -> assertEquals(1, actual.getShape().size()),
                () -> assertEquals(4, actual.getFft().size()),
                () -> assertEquals(2, actual.getSensorDataPointIdList().size()),
                () -> assertEquals(new BigDecimal(2.0).doubleValue(), actual.getCrest().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(2.1).doubleValue(), actual.getCrest().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(1.0).doubleValue(), actual.getShape().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(3.0).doubleValue(), actual.getRms().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(3.1).doubleValue(), actual.getRms().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(4.0).doubleValue(), actual.getKurtosis().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(4.1).doubleValue(), actual.getKurtosis().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(1.2).doubleValue(), actual.getFft().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.3).doubleValue(), actual.getFft().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(2.2).doubleValue(), actual.getFft().get(2).doubleValue()),
                () -> assertEquals(new BigDecimal(2.3).doubleValue(), actual.getFft().get(3).doubleValue()),
                () -> assertEquals("test id one", actual.getSensorDataPointIdList().get(0)),
                () -> assertEquals("test id two", actual.getSensorDataPointIdList().get(1))
        );
    }

    @Test
    @DisplayName("should return a valid DspDataList from List<DspDataPointModel> skipping missing FFT data")
    public void test_toDspDataList_mapping_missing_FFT_data() {
        // Arrange
        List<DspDataPointModel> mockList = getDspDataPointModelList();
        mockList.get(0).setFft(null);

        // Act
        DspDataList actual = mapperToTest.toDspDataList(mockList);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(2, actual.getCrest().size()),
                () -> assertEquals(2, actual.getKurtosis().size()),
                () -> assertEquals(2, actual.getRms().size()),
                () -> assertEquals(2, actual.getShape().size()),
                () -> assertEquals(2, actual.getFft().size()),
                () -> assertEquals(2, actual.getSensorDataPointIdList().size()),
                () -> assertEquals(new BigDecimal(2.0).doubleValue(), actual.getCrest().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(2.1).doubleValue(), actual.getCrest().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(1.0).doubleValue(), actual.getShape().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.1).doubleValue(), actual.getShape().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(3.0).doubleValue(), actual.getRms().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(3.1).doubleValue(), actual.getRms().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(4.0).doubleValue(), actual.getKurtosis().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(4.1).doubleValue(), actual.getKurtosis().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(2.2).doubleValue(), actual.getFft().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(2.3).doubleValue(), actual.getFft().get(1).doubleValue()),
                () -> assertEquals("test id one", actual.getSensorDataPointIdList().get(0)),
                () -> assertEquals("test id two", actual.getSensorDataPointIdList().get(1))
        );
    }

    @Test
    @DisplayName("should return a valid DspDataList from List<DspDataPointModel> skipping missing SensorDataPointId data")
    public void test_toDspDataList_mapping_missing_SensorDataPointId_data() {
        // Arrange
        List<DspDataPointModel> mockList = getDspDataPointModelList();
        mockList.get(0).setSensorDataPointId(null);

        // Act
        DspDataList actual = mapperToTest.toDspDataList(mockList);

        // Assert
        assertAll("ensure ok",
                () -> assertEquals(2, actual.getCrest().size()),
                () -> assertEquals(2, actual.getKurtosis().size()),
                () -> assertEquals(2, actual.getRms().size()),
                () -> assertEquals(2, actual.getShape().size()),
                () -> assertEquals(4, actual.getFft().size()),
                () -> assertEquals(1, actual.getSensorDataPointIdList().size()),
                () -> assertEquals(new BigDecimal(2.0).doubleValue(), actual.getCrest().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(2.1).doubleValue(), actual.getCrest().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(1.0).doubleValue(), actual.getShape().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.1).doubleValue(), actual.getShape().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(3.0).doubleValue(), actual.getRms().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(3.1).doubleValue(), actual.getRms().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(4.0).doubleValue(), actual.getKurtosis().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(4.1).doubleValue(), actual.getKurtosis().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(1.2).doubleValue(), actual.getFft().get(0).doubleValue()),
                () -> assertEquals(new BigDecimal(1.3).doubleValue(), actual.getFft().get(1).doubleValue()),
                () -> assertEquals(new BigDecimal(2.2).doubleValue(), actual.getFft().get(2).doubleValue()),
                () -> assertEquals(new BigDecimal(2.3).doubleValue(), actual.getFft().get(3).doubleValue()),
                () -> assertEquals("test id two", actual.getSensorDataPointIdList().get(0))
        );
    }

    private List<DspDataPointModel> getDspDataPointModelList(){

        List<DspDataPointModel> mockList = new ArrayList<>();

        DspDataPointModel dataPointModelOne = DspDataPointModel.builder()
                .sensorId("test_id")
                .fft(mockFftOne())
                .shape(1.0)
                .crest(2.0)
                .rms(3.0)
                .kurtosis(4.0)
                .sensorDataPointId("test id one")
                .sensorId("test sensor id")
                .build();

        DspDataPointModel dataPointModelTwo = DspDataPointModel.builder()
                .sensorId("test_id")
                .fft(mockFftTwo())
                .shape(1.1)
                .crest(2.1)
                .rms(3.1)
                .kurtosis(4.1)
                .sensorDataPointId("test id two")
                .sensorId("test sensor id")
                .build();

        mockList.add(dataPointModelOne);
        mockList.add(dataPointModelTwo);
        return mockList;
    }

    private List<Double> mockFftOne() {
        List<Double> mockFftOne = new ArrayList<>();
        mockFftOne.add(1.2);
        mockFftOne.add(1.3);
        return mockFftOne;
    }

    private List<Double> mockFftTwo() {
        List<Double> mockFftTwo = new ArrayList<>();
        mockFftTwo.add(2.2);
        mockFftTwo.add(2.3);
        return mockFftTwo;
    }
}
