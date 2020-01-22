package com.vape.data.streaming.service;

import com.vape.data.streaming.model.KurtosisDataPointModel;
import com.vape.data.streaming.model.RMSDataPointModel;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.utility.JsonMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class DataPointConsumer {

    private final JsonMapper mapper;

    private final DspEngineService dspEngineService;

    private final DataPointProducer dataPointProducer;

    private final static String LOG_MSG = "#### -> Consumed message from SENSOR -> %s ####";

//    @KafkaListener(topics = "SENSOR", groupId = "consumer-group-streaming-fft")
//    public void computeFFT(String message) throws IOException {
//        log.info(String.format(LOG_MSG, message));
//    }

    @KafkaListener(topics = "SENSOR", groupId = "consumer-group-streaming-rms")
    public void computeRMS(String message) throws IOException {
        log.info(String.format(LOG_MSG, message));
        SensorDataPointModel dataPointModel = mapper.toObject(message, SensorDataPointModel.class);
        BigDecimal computedRMS = dspEngineService.computeRMS(dataPointModel);
        RMSDataPointModel rmsDataPointModel = constructRMSDataPointModel(dataPointModel, computedRMS);
        dataPointProducer.publishRMS(rmsDataPointModel);
    }

    @KafkaListener(topics = "SENSOR", groupId = "consumer-group-streaming-kurtosis")
    public void computeKurtosis(String message) throws IOException {
        log.info(String.format(LOG_MSG, message));
        SensorDataPointModel dataPointModel = mapper.toObject(message, SensorDataPointModel.class);
        BigDecimal computedKurtosis = dspEngineService.computeKurtosis(dataPointModel);
        KurtosisDataPointModel kurtosisDataPointModel = constructKurtosisDataPointModel(dataPointModel, computedKurtosis);
        dataPointProducer.publishKurtosis(kurtosisDataPointModel);
    }

    KurtosisDataPointModel constructKurtosisDataPointModel(SensorDataPointModel dataPointModel, BigDecimal computedData) {
        return KurtosisDataPointModel.builder()
                .sensorDataPointId(dataPointModel.getId())
                .timestamp(LocalDateTime.now().toString())
                .data(computedData)
                .build();
    }

    RMSDataPointModel constructRMSDataPointModel(SensorDataPointModel dataPointModel, BigDecimal computedData) {
        return RMSDataPointModel.builder()
                .sensorDataPointId(dataPointModel.getId())
                .timestamp(LocalDateTime.now().toString())
                .data(computedData)
                .build();
    }
}
