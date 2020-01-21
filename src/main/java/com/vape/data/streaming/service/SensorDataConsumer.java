package com.vape.data.streaming.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class SensorDataConsumer {

    @KafkaListener(topics = "SENSOR_DATA", groupId = "consumer-group-streaming-fft")
    public void computeFFT(String message) throws IOException {
        log.info(String.format("#### -> Consumed message from SENSOR_DATA -> %s ####", message));
    }

    @KafkaListener(topics = "SENSOR_DATA", groupId = "consumer-group-streaming-rms")
    public void computeRMS(String message) throws IOException {
        log.info(String.format("#### -> Consumed message from SENSOR_DATA -> %s", message));
    }

    @KafkaListener(topics = "SENSOR_DATA", groupId = "consumer-group-streaming-kurtosis")
    public void computeKurtosis(String message) throws IOException {
        log.info(String.format("#### -> Consumed message from SENSOR_DATA -> %s", message));
    }
}
