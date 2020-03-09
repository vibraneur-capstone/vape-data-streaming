package com.vape.data.streaming.controller;

import com.vape.data.streaming.mapper.SensorDataPointModelMapper;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.service.DataPointProducer;
import com.vape.data.streaming.service.DataQueryService;
import com.vape.data.streaming.swagger.v1.api.SensorApi;
import com.vape.data.streaming.swagger.v1.model.SensorData;
import com.vape.data.streaming.swagger.v1.model.SensorDataList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/")
@CrossOrigin()
@AllArgsConstructor
@Slf4j
public class SensorDataController implements SensorApi {

    private final DataPointProducer producer;
    private final DataQueryService dataQueryService;
    private final SensorDataPointModelMapper mapper;

    @Override
    public ResponseEntity<SensorData> sensorPost(SensorData body, String sensorId) {
        if (StringUtils.isNotEmpty(body.getSensorId()) && sensorId.equals(body.getSensorId()) ) {
            SensorDataPointModel model = mapper.toSensorDataPointModel(body);
            producer.publishSensorData(model);
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        return new ResponseEntity<>(body , HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<SensorDataList> sensorGet(String sensorId, Integer from, Integer to) {
        List<SensorDataPointModel> sensorDataPointModels = dataQueryService.getSensorDataByIdAndDateRange(sensorId, from, to);
        System.out.println(sensorDataPointModels.size());
        SensorDataList sensorDataList = mapper.toSensorDataList(sensorDataPointModels);
        sensorDataList.setFrom(from);
        sensorDataList.setTo(to);
        sensorDataList.setSensorId(sensorId);
        return new ResponseEntity<>(sensorDataList, HttpStatus.OK);
    }
}