package com.vape.data.streaming.controller;

import com.vape.data.streaming.config.KafkaConsumerConfig;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.service.DataPointProducer;
import com.vape.dsp.integration.swagger.v1.model.DspDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
@CrossOrigin()
public class HealthCheck {

    @Autowired
    private DataPointProducer producer;

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
            consumes = "application/json",
            method = RequestMethod.POST)
    public ResponseEntity<String> testProducer(@Valid @RequestBody DspDataInput input) {
        SensorDataPointModel sensor = SensorDataPointModel.builder().sensorHubId("sensor hub one").sensorId("sensor one").data(input.getData()).build();
        producer.publishSensorData(sensor);
        return new ResponseEntity<>(config.getBootstrapServers(), HttpStatus.OK);
    }

}
