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

//    @KafkaListener(topics = "SENSOR", groupId = "consumer-group-streaming-dsp")
//    public void computeRMS(String message) throws IOException {
//        log.info(String.format(LOG_MSG, "consumer rms", message));
//        SensorDataPointModel dataPointModel = mapper.toObject(message, SensorDataPointModel.class);
//        BigDecimal computedRMS = dspEngineService.computeRMS(dataPointModel);
//        RMSDataPointModel rmsDataPointModel = modelMapper.constructRMSDataPointModel(dataPointModel, computedRMS);
//        dataPointProducer.publishRMS(rmsDataPointModel);
//    }
}
