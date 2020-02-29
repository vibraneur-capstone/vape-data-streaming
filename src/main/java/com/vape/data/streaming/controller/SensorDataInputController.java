package com.vape.data.streaming.controller;

import com.vape.data.streaming.mapper.DtoMapper;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.service.DataPointProducer;
import com.vape.data.streaming.swagger.v1.api.SensorApi;
import com.vape.data.streaming.swagger.v1.model.SensorData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
@CrossOrigin()
@AllArgsConstructor
@Slf4j
public class SensorDataInputController implements SensorApi {

    private final DataPointProducer producer;
    private final DtoMapper mapper;

    @Override
    public ResponseEntity<SensorData> sensorPost(SensorData body, String id) {
        if (StringUtils.isNotEmpty(body.getSensorId()) && id.equals(body.getSensorId()) ) {
            SensorDataPointModel model = mapper.toSensorDataPointModel(body);
            producer.publishSensorData(model);
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        return new ResponseEntity<>(body , HttpStatus.BAD_REQUEST);
    }
}
/*
CompletableFuture<List<Double>> tmp = dspEngineService.computeFreqDomain(DspTopic.FFT, model);
            try{
                log.info(tmp.get().toString());
            }
            catch (ExecutionException | InterruptedException ex) {

            }
 */