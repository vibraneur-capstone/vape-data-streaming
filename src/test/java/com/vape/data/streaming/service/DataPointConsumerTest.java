package com.vape.data.streaming.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vape.data.streaming.mapper.ModelMapper;
import com.vape.data.streaming.model.*;
import com.vape.data.streaming.utility.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DataPointConsumerTest {

    @Spy
    @InjectMocks
    private DataPointConsumer serviceToTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private JsonMapper mapper;

    @Mock
    private DspEngineService dspEngineService;

    @Mock
    private DataPointProducer dataPointProducer;

    @Mock
    private ModelMapper modelMapper;

    @Test
    @DisplayName("should compute RMS and publish to Kafka")
    void test_rms() throws JsonProcessingException, IOException {
        // Arrange
        String kafkaMessage = "{\"id\":\"567\",\"sensorId\":\"123\",\"sensorHubId\":null,\"timestamp\":null,\"data\":[123,321]}";
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().id("567").sensorId("123").build();
        RMSDataPointModel constructedModel = RMSDataPointModel.builder().sensorDataPointId("567").data(1.0).build();
        BigDecimal computedRMS = new BigDecimal(1.3);

        when(mapper.toObject(kafkaMessage, SensorDataPointModel.class)).thenReturn(sensorDataPointModel);
        when(modelMapper.constructRMSDataPointModel(sensorDataPointModel, computedRMS)).thenReturn(constructedModel);
        when(dspEngineService.computeRMS(sensorDataPointModel)).thenReturn(computedRMS);

        // Act
        serviceToTest.computeRMS(kafkaMessage);

        // Assert
        verify(mapper, times(1)).toObject(eq(kafkaMessage), eq(SensorDataPointModel.class));
        verify(dspEngineService, times(1)).computeRMS(sensorDataPointModel);
        verify(modelMapper, times(1)).constructRMSDataPointModel(sensorDataPointModel, computedRMS);
        verify(dataPointProducer, times(1)).publishRMS(constructedModel);
    }

    @Test
    @DisplayName("should compute kurtosis and publish to Kafka")
    void test_kurtosis() throws IOException {
        // Arrange
        String kafkaMessage = "{\"id\":\"567\",\"sensorId\":\"123\",\"sensorHubId\":null,\"timestamp\":null,\"data\":[123,321]}";
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().id("567").sensorId("123").build();
        KurtosisDataPointModel constructedModel = KurtosisDataPointModel.builder().sensorDataPointId("567").data(1.0).build();
        BigDecimal computedKurtosis = new BigDecimal(1.3);

        when(mapper.toObject(kafkaMessage, SensorDataPointModel.class)).thenReturn(sensorDataPointModel);
        when(modelMapper.constructKurtosisDataPointModel(sensorDataPointModel, computedKurtosis)).thenReturn(constructedModel);
//        doReturn(constructedModel).when(serviceToTest).constructKurtosisDataPointModel(sensorDataPointModel, computedKurtosis);
        when(dspEngineService.computeKurtosis(sensorDataPointModel)).thenReturn(computedKurtosis);

        // Act
        serviceToTest.computeKurtosis(kafkaMessage);

        // Assert
        verify(mapper, times(1)).toObject(eq(kafkaMessage), eq(SensorDataPointModel.class));
        verify(dspEngineService, times(1)).computeKurtosis(sensorDataPointModel);
        verify(modelMapper, times(1)).constructKurtosisDataPointModel(sensorDataPointModel, computedKurtosis);
        verify(dataPointProducer, times(1)).publishKurtosis(constructedModel);
    }

    @Test
    @DisplayName("should compute crest and publish to Kafka")
    void test_crest() throws IOException {
        // Arrange
        String kafkaMessage = "{\"id\":\"567\",\"sensorId\":\"123\",\"sensorHubId\":null,\"timestamp\":null,\"data\":[123,321]}";
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().id("567").sensorId("123").build();
        CrestDataPointModel constructedModel = CrestDataPointModel.builder().sensorDataPointId("567").data(1.0).build();
        BigDecimal computedCrest = new BigDecimal(1.3);

        when(mapper.toObject(kafkaMessage, SensorDataPointModel.class)).thenReturn(sensorDataPointModel);
        when(modelMapper.constructCrestDataPointModel(sensorDataPointModel, computedCrest)).thenReturn(constructedModel);
        when(dspEngineService.computeCrest(sensorDataPointModel)).thenReturn(computedCrest);

        // Act
        serviceToTest.computeCrest(kafkaMessage);

        // Assert
        verify(mapper, times(1)).toObject(eq(kafkaMessage), eq(SensorDataPointModel.class));
        verify(dspEngineService, times(1)).computeCrest(sensorDataPointModel);
        verify(modelMapper, times(1)).constructCrestDataPointModel(sensorDataPointModel, computedCrest);
        verify(dataPointProducer, times(1)).publishCrest(constructedModel);
    }

    @Test
    @DisplayName("should compute shape and publish to Kafka")
    void test_shape() throws IOException {
        // Arrange
        String kafkaMessage = "{\"id\":\"567\",\"sensorId\":\"123\",\"sensorHubId\":null,\"timestamp\":null,\"data\":[123,321]}";
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().id("567").sensorId("123").build();
        ShapeDataPointModel constructedModel = ShapeDataPointModel.builder().sensorDataPointId("567").data(1.0).build();
        BigDecimal computedShape = new BigDecimal(1.3);

        when(mapper.toObject(kafkaMessage, SensorDataPointModel.class)).thenReturn(sensorDataPointModel);
        when(modelMapper.constructShapeDataPointModel(sensorDataPointModel, computedShape)).thenReturn(constructedModel);
        when(dspEngineService.computeShape(sensorDataPointModel)).thenReturn(computedShape);

        // Act
        serviceToTest.computeShape(kafkaMessage);

        // Assert
        verify(mapper, times(1)).toObject(eq(kafkaMessage), eq(SensorDataPointModel.class));
        verify(dspEngineService, times(1)).computeShape(sensorDataPointModel);
        verify(modelMapper, times(1)).constructShapeDataPointModel(sensorDataPointModel, computedShape);
        verify(dataPointProducer, times(1)).publishShape(constructedModel);
    }


}
