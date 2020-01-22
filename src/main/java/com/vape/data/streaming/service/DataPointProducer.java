package com.vape.data.streaming.service;

import com.vape.data.streaming.model.*;
import com.vape.data.streaming.repository.KurtosisDataPointModelRepository;
import com.vape.data.streaming.repository.RMSDataPointModelRepository;
import com.vape.data.streaming.repository.SensorDataPointModelRepository;
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

    private final RMSDataPointModelRepository rmsDataPointModelRepository;

    private final KurtosisDataPointModelRepository kurtosisDataPointModelRepository;

    public void publishSensorData(SensorDataPointModel dataPoint) throws InvalidTopicException {
        dataPoint.setTimestamp(LocalDateTime.now().toString());
        dataPoint = sensorDataPointModelRepository.save(dataPoint);
        log.info("#### -> Publishing Sensor data point for sensor data ####");
        publishMessage(KafkaTopic.SENSOR.toString(), mapper.toJson(dataPoint));
    }

    public void publishKurtosis(KurtosisDataPointModel kurtosis) {
        log.info(String.format("#### -> Publishing Kurtosis data point for sensor data (id:: %s) ####", kurtosis.getSensorDataPointId()));
        KurtosisDataPointModel savedKurtosis = kurtosisDataPointModelRepository.save(kurtosis);
        publishMessage(KafkaTopic.KURTOSIS.toString(), mapper.toJson(savedKurtosis));
    }

    public void publishRMS(RMSDataPointModel rms) {
        log.info(String.format("#### -> Publishing RMS data point for sensor data (id:: %s) ####", rms.getSensorDataPointId()));
        RMSDataPointModel savedRMS = rmsDataPointModelRepository.save(rms);
        publishMessage(KafkaTopic.RMS.toString(), mapper.toJson(savedRMS));
    }

    public void publishFFT(FFTDataPointModel fft) {
        log.info(String.format("#### -> Publishing FFT data point for sensor data (id:: %s) ####", fft.getSensorDataPointId()));
        publishMessage(KafkaTopic.FFT.toString(), mapper.toJson(fft));
    }

    private void publishMessage(String topic, String msg) throws InvalidTopicException {
        this.kafkaTemplate.send(topic, msg);
    }
}
