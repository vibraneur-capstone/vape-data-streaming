package com.vape.data.streaming.service;

import com.vape.data.streaming.model.*;
import com.vape.data.streaming.repository.*;
import com.vape.data.streaming.utility.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DataPointProducerTest {
    @InjectMocks
    private DataPointProducer serviceToTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private JsonMapper mapper;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private SensorDataPointModelRepository sensorDataPointModelRepository;

    @Mock
    private RMSDataPointModelRepository rmsDataPointModelRepository;

    @Mock
    private KurtosisDataPointModelRepository kurtosisDataPointModelRepository;

    @Mock
    private CrestDataPointModelRepository crestDataPointModelRepository;

    @Mock
    private ShapeDataPointModelRepository shapeDataPointModelRepository;

    @Test
    @DisplayName("should store raw data in mongo then publish stored data to topic: SENSOR")
    void test_publishSensorData_method() {
        // Arrange
        SensorDataPointModel incomingSensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        SensorDataPointModel mongoSensorDataPointModel = SensorDataPointModel.builder().id("123").sensorId("123").build();
        String expectedMsg = "test message";

        when(sensorDataPointModelRepository.save(incomingSensorDataPointModel)).thenReturn(mongoSensorDataPointModel);
        when(mapper.toJson(mongoSensorDataPointModel)).thenReturn(expectedMsg);

        // Act
        serviceToTest.publishSensorData(incomingSensorDataPointModel);

        // Assert
        assertAll("ensure OK",
                () -> assertNotNull(incomingSensorDataPointModel.getTimestamp())
        );
        verify(sensorDataPointModelRepository, times(1)).save(incomingSensorDataPointModel);
        verify(kafkaTemplate, times(1)).send(eq("SENSOR"), eq(expectedMsg));
    }

    @Test
    @DisplayName("should publish FFT data")
    void test_publish_publishFFT_method() {
        // Arrange
        FFTDataPointModel fftDataPointModel = FFTDataPointModel.builder().id("test id").build();
        String expectedMsg = "test message";

        when(mapper.toJson(fftDataPointModel)).thenReturn(expectedMsg);

        // Act
        serviceToTest.publishFFT(fftDataPointModel);

        // Assert
        verify(kafkaTemplate, times(1)).send(eq("FFT"), eq(expectedMsg));
    }

    @Test
    @DisplayName("should publish RMS data and persist in Mongo")
    void test_publish_publishRMS_method() {
        // Arrange
        RMSDataPointModel rmsDataPointModel = RMSDataPointModel.builder().sensorDataPointId("123").build();
        RMSDataPointModel savedDataPointModel = RMSDataPointModel.builder().id("test id").sensorDataPointId("123").build();

        String expectedMsg = "test message";

        when(rmsDataPointModelRepository.save(rmsDataPointModel)).thenReturn(savedDataPointModel);
        when(mapper.toJson(savedDataPointModel)).thenReturn(expectedMsg);

        // Act
        serviceToTest.publishRMS(rmsDataPointModel);

        // Assert
        verify(rmsDataPointModelRepository, times(1)).save(rmsDataPointModel);
        verify(kafkaTemplate, times(1)).send(eq("RMS"), eq(expectedMsg));
    }

    @Test
    @DisplayName("should publish Kurtosis and persist in mongo")
    void test_publish_publishKurtosis_method() {
        // Arrange
        KurtosisDataPointModel kurtosisDataPointModel = KurtosisDataPointModel.builder().sensorDataPointId("123").build();
        KurtosisDataPointModel savedDataPointModel = KurtosisDataPointModel.builder().sensorDataPointId("123").id("test id").build();

        String expectedMessage = "This is a test message benxin is the best";

        when(kurtosisDataPointModelRepository.save(kurtosisDataPointModel)).thenReturn(savedDataPointModel);
        when(mapper.toJson(savedDataPointModel)).thenReturn(expectedMessage);

        // Act
        serviceToTest.publishKurtosis(kurtosisDataPointModel);

        // Assert
        verify(kurtosisDataPointModelRepository, times(1)).save(kurtosisDataPointModel);
        verify(kafkaTemplate, times(1)).send(eq("KURTOSIS"), eq(expectedMessage));
    }

    @Test
    @DisplayName("should publish crest and persist in mongo")
    void test_publish_publishCrest_method() {
        // Arrange
        CrestDataPointModel crestDataPointModel = CrestDataPointModel.builder().sensorDataPointId("123").build();
        CrestDataPointModel savedDataPointModel = CrestDataPointModel.builder().sensorDataPointId("123").id("test id").build();

        String expectedMessage = "This is a test message benxin is the best";

        when(crestDataPointModelRepository.save(crestDataPointModel)).thenReturn(savedDataPointModel);
        when(mapper.toJson(savedDataPointModel)).thenReturn(expectedMessage);

        // Act
        serviceToTest.publishCrest(crestDataPointModel);

        // Assert
        verify(crestDataPointModelRepository, times(1)).save(crestDataPointModel);
        verify(kafkaTemplate, times(1)).send(eq("CREST"), eq(expectedMessage));
    }

    @Test
    @DisplayName("should publish shape and persist in mongo")
    void test_publish_publishShape_method() {
        // Arrange
        ShapeDataPointModel shapeDataPointModel = ShapeDataPointModel.builder().sensorDataPointId("123").build();
        ShapeDataPointModel savedDataPointModel = ShapeDataPointModel.builder().sensorDataPointId("123").id("test id").build();

        String expectedMessage = "This is a test message benxin is the best";

        when(shapeDataPointModelRepository.save(shapeDataPointModel)).thenReturn(savedDataPointModel);
        when(mapper.toJson(savedDataPointModel)).thenReturn(expectedMessage);

        // Act
        serviceToTest.publishShape(shapeDataPointModel);

        // Assert
        verify(shapeDataPointModelRepository, times(1)).save(shapeDataPointModel);
        verify(kafkaTemplate, times(1)).send(eq("SHAPE"), eq(expectedMessage));
    }
}
