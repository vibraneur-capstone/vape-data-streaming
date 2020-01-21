package com.vape.data.streaming.service;

import com.vape.data.streaming.model.FFTDataPointModel;
import com.vape.data.streaming.model.KafkaTopic;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.repository.SensorDataPointRepository;
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

    private final SensorDataPointRepository sensorDataPointRepository;

    public void publishSensorData(SensorDataPointModel dataPoint) throws InvalidTopicException {
        dataPoint.setTimestamp(LocalDateTime.now());
        dataPoint = sensorDataPointRepository.save(dataPoint);
        log.info("#### -> Publishing Sensor data point for sensor data ####");
        publishMessage(KafkaTopic.SENSOR.toString(), mapper.toJson(dataPoint));
    }

    public void publishFFT(FFTDataPointModel fft) {
        log.info(String.format("#### -> Publishing FFT data point for sensor data (id:: %s) ####", fft.getSensorDataPointId()));
        publishMessage(KafkaTopic.FFT.toString(), mapper.toJson(fft));
    }

    private void publishMessage(String topic, String msg) throws InvalidTopicException {
        this.kafkaTemplate.send(topic, msg);
    }
}
