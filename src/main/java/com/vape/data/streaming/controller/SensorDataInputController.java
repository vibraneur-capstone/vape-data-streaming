package com.vape.data.streaming.controller;

import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.service.DataPointProducer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
@CrossOrigin()
@AllArgsConstructor
public class SensorDataInputController {

    private DataPointProducer producer;

    @RequestMapping(value = "/sensor",
            produces = "application/json",
            consumes = "application/json",
            method = RequestMethod.POST)
    public ResponseEntity<SensorDataPointModel> testProducer(@Valid @RequestBody SensorDataPointModel input) {
        producer.publishSensorData(input);
        return new ResponseEntity<>(input, HttpStatus.OK);
    }

}
