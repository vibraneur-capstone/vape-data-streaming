package com.vape.data.streaming.service;

import com.vape.data.streaming.config.WebSocketConfig;
import com.vape.data.streaming.model.*;
import com.vape.data.streaming.utility.JsonMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@AllArgsConstructor
public class DataPointConsumer {

    private final JsonMapper mapper;

    private final DspEngineService dspEngineService;

    private final DataPointProducer dataPointProducer;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final static String LOG_MSG = "#### -> %s Consumed message from SENSOR -> sensor id: %s ####";

    @KafkaListener(topics = "SENSOR", groupId = "consumer-group-streaming-dsp")
    public void computeAllDsp(String message) throws IOException {
        SensorDataPointModel dataPointModel = mapper.toObject(message, SensorDataPointModel.class);
        log.info(String.format(LOG_MSG, "consumer-group-streaming-dsp", dataPointModel.getSensorId()));
        DspDataPointModel dspDataPointModel = computeDspDataPointModel(dataPointModel);
        if (dspDataPointModel != null) {
            DspDataPointModel publishedMsg = dataPointProducer.publishDspData(dspDataPointModel);
            this.simpMessagingTemplate.convertAndSend(WebSocketConfig.APP_DESTINATION_PREFIX + "/dsp", publishedMsg);
        }
    }

    private DspDataPointModel computeDspDataPointModel(SensorDataPointModel dataPointModel) {
        CompletableFuture<Double> rms = dspEngineService.computeTimeDomain(DspTopic.RMS, dataPointModel);
        CompletableFuture<Double> kurtosis = dspEngineService.computeTimeDomain(DspTopic.KURTOSIS, dataPointModel);
        CompletableFuture<Double> crest = dspEngineService.computeTimeDomain(DspTopic.CREST, dataPointModel);
        CompletableFuture<Double> shape = dspEngineService.computeTimeDomain(DspTopic.SHAPE, dataPointModel);
        CompletableFuture<List<Double>> fft = dspEngineService.computeFreqDomain(DspTopic.FFT, dataPointModel);
        try {
            return DspDataPointModel.builder()
                    .sensorDataPointId(dataPointModel.getId())
                    .sensorId(dataPointModel.getSensorId())
                    .crest(crest.get())
                    .kurtosis(kurtosis.get())
                    .rms(rms.get())
                    .shape(shape.get())
                    .fft(fft.get())
                    .build();
        }
        catch (ExecutionException | InterruptedException ex) {
            log.error("Exception occurred during getting DSP result");
            return null;
        }
    }
}
