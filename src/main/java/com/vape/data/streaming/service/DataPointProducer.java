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

    private final RMSDataPointModelRepository rmsDataPointModelRepository;

    private final KurtosisDataPointModelRepository kurtosisDataPointModelRepository;

    private final CrestDataPointModelRepository crestDataPointModelRepository;

    private final ShapeDataPointModelRepository shapeDataPointModelRepository;

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

    public void publishCrest(CrestDataPointModel crest) {
        log.info(String.format("#### -> Publishing Crest data point for sensor data (id:: %s) ####", crest.getSensorDataPointId()));
        CrestDataPointModel savedCrest = crestDataPointModelRepository.save(crest);
        publishMessage(KafkaTopic.CREST.toString(), mapper.toJson(savedCrest));
    }

    public void publishShape(ShapeDataPointModel shape) {
        log.info(String.format("#### -> Publishing Shape data point for sensor data (id:: %s) ####", shape.getSensorDataPointId()));
        ShapeDataPointModel savedShape = shapeDataPointModelRepository.save(shape);
        publishMessage(KafkaTopic.SHAPE.toString(), mapper.toJson(savedShape));
    }

    private void publishMessage(String topic, String msg) throws InvalidTopicException {
        this.kafkaTemplate.send(topic, msg);
    }
}
