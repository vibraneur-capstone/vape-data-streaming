package com.vape.data.streaming.service;

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
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

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

    @Test
    @DisplayName("should compute DSP asynchronously and publish to Kafka")
    void test_compute_dsp() throws IOException {
        // Arrange
        String kafkaMessage = "{\"id\":\"567\",\"sensorId\":\"123\",\"sensorHubId\":null,\"timestamp\":null,\"data\":[123,321]}";
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        SensorDataPointModel publishedSensorDataPointModel = SensorDataPointModel.builder().timestamp("test").id("567").sensorId("123").build();
        DspDataPointModel expectedModel = DspDataPointModel.builder()
                .sensorDataPointId("567")
                .sensorId("123")
                .crest(1.2)
                .kurtosis(0.2)
                .rms(2.2)
                .shape(4.2)
                .fft(new ArrayList<>())
                .build();

        when(mapper.toObject(kafkaMessage, SensorDataPointModel.class)).thenReturn(sensorDataPointModel);

        when(dataPointProducer.publishSensorData(sensorDataPointModel)).thenReturn(publishedSensorDataPointModel);

        when(dspEngineService.computeTimeDomain(DspTopic.RMS, publishedSensorDataPointModel)).thenReturn(CompletableFuture.completedFuture(2.2));
        when(dspEngineService.computeTimeDomain(DspTopic.KURTOSIS, publishedSensorDataPointModel)).thenReturn(CompletableFuture.completedFuture(0.2));
        when(dspEngineService.computeTimeDomain(DspTopic.CREST, publishedSensorDataPointModel)).thenReturn(CompletableFuture.completedFuture(1.2));
        when(dspEngineService.computeTimeDomain(DspTopic.SHAPE, publishedSensorDataPointModel)).thenReturn(CompletableFuture.completedFuture(4.2));
        when(dspEngineService.computeFreqDomain(DspTopic.FFT, publishedSensorDataPointModel)).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));

        // Act
        serviceToTest.computeAllDsp(kafkaMessage);

        // Assert
        verify(mapper, times(1)).toObject(eq(kafkaMessage), eq(SensorDataPointModel.class));

        verify(dspEngineService, times(1)).computeTimeDomain(DspTopic.RMS, publishedSensorDataPointModel);
        verify(dspEngineService, times(1)).computeTimeDomain(DspTopic.KURTOSIS, publishedSensorDataPointModel);
        verify(dspEngineService, times(1)).computeTimeDomain(DspTopic.CREST, publishedSensorDataPointModel);
        verify(dspEngineService, times(1)).computeTimeDomain(DspTopic.SHAPE, publishedSensorDataPointModel);
        verify(dspEngineService, times(1)).computeFreqDomain(DspTopic.FFT, publishedSensorDataPointModel);

        verify(dataPointProducer, times(1)).publishSensorData(sensorDataPointModel);
        verify(dataPointProducer, times(1)).publishDspData(eq(expectedModel));
    }

    @Test
    @DisplayName("should not publish data when exception occured")
    void test_bad_dsp_exception() throws IOException, ExecutionException, InterruptedException {
        // Arrange
        String kafkaMessage = "{\"id\":\"567\",\"sensorId\":\"123\",\"sensorHubId\":null,\"timestamp\":null,\"data\":[123,321]}";
        SensorDataPointModel sensorDataPointModel = SensorDataPointModel.builder().sensorId("123").build();
        SensorDataPointModel publishedSensorDataPointModel = SensorDataPointModel.builder().timestamp("test").id("567").sensorId("123").build();
        DspDataPointModel expectedModel = null;
        CompletableFuture badFuture = mock(CompletableFuture.class);

        when(mapper.toObject(kafkaMessage, SensorDataPointModel.class)).thenReturn(sensorDataPointModel);

        when(dataPointProducer.publishSensorData(sensorDataPointModel)).thenReturn(publishedSensorDataPointModel);

        when(dspEngineService.computeTimeDomain(DspTopic.RMS, publishedSensorDataPointModel)).thenReturn(CompletableFuture.completedFuture(2.2));
        when(dspEngineService.computeTimeDomain(DspTopic.KURTOSIS, publishedSensorDataPointModel)).thenReturn(CompletableFuture.completedFuture(0.2));
        when(dspEngineService.computeTimeDomain(DspTopic.CREST, publishedSensorDataPointModel)).thenReturn(CompletableFuture.completedFuture(1.2));
        when(dspEngineService.computeTimeDomain(DspTopic.SHAPE, publishedSensorDataPointModel)).thenReturn(badFuture);
        when(dspEngineService.computeFreqDomain(DspTopic.FFT, publishedSensorDataPointModel)).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));
        when(badFuture.get()).thenThrow(new InterruptedException());

        // Act
        serviceToTest.computeAllDsp(kafkaMessage);

        // Assert
        verify(mapper, times(1)).toObject(eq(kafkaMessage), eq(SensorDataPointModel.class));

        verify(dspEngineService, times(1)).computeTimeDomain(DspTopic.RMS, publishedSensorDataPointModel);
        verify(dspEngineService, times(1)).computeTimeDomain(DspTopic.KURTOSIS, publishedSensorDataPointModel);
        verify(dspEngineService, times(1)).computeTimeDomain(DspTopic.CREST, publishedSensorDataPointModel);
        verify(dspEngineService, times(1)).computeTimeDomain(DspTopic.SHAPE, publishedSensorDataPointModel);
        verify(dspEngineService, times(1)).computeFreqDomain(DspTopic.FFT, publishedSensorDataPointModel);

        verify(dataPointProducer, times(1)).publishSensorData(sensorDataPointModel);
        verify(dataPointProducer, times(0)).publishDspData(any());
    }
}
