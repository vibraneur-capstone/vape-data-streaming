package com.vape.data.streaming.service;

import com.vape.data.streaming.config.WebSocketConfig;
import com.vape.data.streaming.model.*;
import com.vape.data.streaming.repository.*;
import com.vape.data.streaming.utility.JsonMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.InvalidTopicException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class DataPointProducer {

    private final JsonMapper mapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final SensorDataPointModelRepository sensorDataPointModelRepository;

    private final DspDataPointModelRepository dspDataPointModelRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public SensorDataPointModel publishSensorData(SensorDataPointModel dataPoint) throws InvalidTopicException {
        dataPoint.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toString());
        SensorDataPointModel savedDataPoint = sensorDataPointModelRepository.save(dataPoint);
        log.info(String.format("#### -> Publishing Sensor data point for data point id: %s ####", savedDataPoint.getId()));
        String message = mapper.toJson(savedDataPoint);
        this.simpMessagingTemplate.convertAndSend(WebSocketConfig.APP_DESTINATION_PREFIX + "/sensor", message);
        publishMessage(KafkaTopic.SENSOR.toString(),message);
        return savedDataPoint;
    }

    public DspDataPointModel publishDspData(DspDataPointModel dspDataPointModel) throws InvalidTopicException {
        dspDataPointModel.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toString());
        DspDataPointModel savedDspDataPoint = dspDataPointModelRepository.save(dspDataPointModel);
        log.info(String.format("#### -> Publishing DSP data point for sensor data point id: %s ####", savedDspDataPoint.getSensorDataPointId()));
        String message = mapper.toJson(savedDspDataPoint);
        this.simpMessagingTemplate.convertAndSend(WebSocketConfig.APP_DESTINATION_PREFIX + "/dsp", message);
        publishMessage(KafkaTopic.DSP.toString(), message);
        return savedDspDataPoint;
    }

    private void publishMessage(String topic, String msg) throws InvalidTopicException {
        this.kafkaTemplate.send(topic, msg);
    }
}
