package com.vape.data.streaming.service;

import com.vape.data.streaming.mapper.ModelMapper;
import com.vape.data.streaming.model.*;
import com.vape.data.streaming.utility.JsonMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Service
@AllArgsConstructor
public class DataPointConsumer {

    private final JsonMapper mapper;

    private final DspEngineService dspEngineService;

    private final DataPointProducer dataPointProducer;

    private final ModelMapper modelMapper;

    private final static String LOG_MSG = "#### -> %s Consumed message from SENSOR -> %s ####";

    @KafkaListener(topics = "SENSOR", groupId = "consumer-group-streaming-rms")
    public void computeRMS(String message) throws IOException {
        log.info(String.format(LOG_MSG, "consumer rms", message));
        SensorDataPointModel dataPointModel = mapper.toObject(message, SensorDataPointModel.class);
        BigDecimal computedRMS = dspEngineService.computeRMS(dataPointModel);
        RMSDataPointModel rmsDataPointModel = modelMapper.constructRMSDataPointModel(dataPointModel, computedRMS);
        dataPointProducer.publishRMS(rmsDataPointModel);
    }

    @KafkaListener(topics = "SENSOR", groupId = "consumer-group-streaming-kurtosis")
    public void computeKurtosis(String message) throws IOException {
        log.info(String.format(LOG_MSG, "consumer kurtosis", message));
        SensorDataPointModel dataPointModel = mapper.toObject(message, SensorDataPointModel.class);
        BigDecimal computedKurtosis = dspEngineService.computeKurtosis(dataPointModel);
        KurtosisDataPointModel kurtosisDataPointModel = modelMapper.constructKurtosisDataPointModel(dataPointModel, computedKurtosis);
        dataPointProducer.publishKurtosis(kurtosisDataPointModel);
    }

    @KafkaListener(topics = "SENSOR", groupId = "consumer-group-streaming-crest")
    public void computeCrest(String message) throws IOException {
        log.info(String.format(LOG_MSG, "consumer crest", message));
        SensorDataPointModel dataPointModel = mapper.toObject(message, SensorDataPointModel.class);
        BigDecimal computedCrest = dspEngineService.computeCrest(dataPointModel);
        CrestDataPointModel crestDataPointModel = modelMapper.constructCrestDataPointModel(dataPointModel, computedCrest);
        dataPointProducer.publishCrest(crestDataPointModel);
    }

    @KafkaListener(topics = "SENSOR", groupId = "consumer-group-streaming-shape")
    public void computeShape(String message) throws IOException {
        log.info(String.format(LOG_MSG, "consumer shape", message));
        SensorDataPointModel dataPointModel = mapper.toObject(message, SensorDataPointModel.class);
        BigDecimal computedShape = dspEngineService.computeShape(dataPointModel);
        ShapeDataPointModel shapeDataPointModel = modelMapper.constructShapeDataPointModel(dataPointModel, computedShape);
        dataPointProducer.publishShape(shapeDataPointModel);
    }
}
