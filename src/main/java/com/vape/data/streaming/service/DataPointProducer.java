package com.vape.data.streaming.service;

import com.vape.data.streaming.model.*;
import com.vape.data.streaming.repository.*;
import com.vape.data.streaming.utility.JsonMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.InvalidTopicException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class DataPointProducer {

    private final JsonMapper mapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final SensorDataPointModelRepository sensorDataPointModelRepository;

    private final DspDataPointModelRepository dspDataPointModelRepository;

    public SensorDataPointModel publishSensorData(SensorDataPointModel dataPoint) throws InvalidTopicException {
        dataPoint.setTimestamp(LocalDateTime.now().toString());
        SensorDataPointModel savedDataPoint = sensorDataPointModelRepository.save(dataPoint);
        log.info(String.format("#### -> Publishing Sensor data point for data point id: %s ####", savedDataPoint.getId()));
        publishMessage(KafkaTopic.SENSOR.toString(), mapper.toJson(savedDataPoint));
        return savedDataPoint;
    }

    public DspDataPointModel publishDspData(DspDataPointModel dspDataPointModel) throws InvalidTopicException {
        dspDataPointModel.setTimestamp(LocalDateTime.now().toString());
        DspDataPointModel savedDspDataPoint = dspDataPointModelRepository.save(dspDataPointModel);
        log.info(String.format("#### -> Publishing DSP data point for sensor data point id: %s ####", savedDspDataPoint.getSensorDataPointId()));
        publishMessage(KafkaTopic.DSP.toString(), mapper.toJson(savedDspDataPoint));
        return savedDspDataPoint;
    }

    private void publishMessage(String topic, String msg) throws InvalidTopicException {
        this.kafkaTemplate.send(topic, msg);
    }
}
