package com.vape.data.streaming.controller;

import com.vape.data.streaming.config.KafkaConsumerConfig;
import com.vape.data.streaming.model.FFTDataPoint;
import com.vape.data.streaming.service.DspDataPointProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin()
public class HealthCheck {

    @Autowired
    private DspDataPointProducer producer;

    @Autowired
    private KafkaConsumerConfig config;

    // TODO: change this...
    @RequestMapping(value = "/vapeHealthCheck",
            produces = "application/json",
            method = RequestMethod.GET)
    public ResponseEntity<String> vapeHealthCheck() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    // TODO: remove this...
    @RequestMapping(value = "/testProducer",
            produces = "application/json",
            method = RequestMethod.GET)
    public ResponseEntity<String> testProducer() {
        FFTDataPoint fft = FFTDataPoint.builder().id("1234").sensorDataPointId("4321").build();
        producer.publishFFT(fft);
        return new ResponseEntity<>(config.getBootstrapServers(), HttpStatus.OK);
    }

}
