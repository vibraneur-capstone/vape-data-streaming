package com.vape.data.streaming.controller;

import com.vape.data.streaming.mapper.DspDataPointModelMapper;
import com.vape.data.streaming.mapper.SensorDataPointModelMapper;
import com.vape.data.streaming.model.DspDataPointModel;
import com.vape.data.streaming.model.KafkaTopic;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.service.DataQueryService;
import com.vape.data.streaming.swagger.v1.api.DataApi;
import com.vape.data.streaming.swagger.v1.model.DataStatus;
import com.vape.data.streaming.swagger.v1.model.DspDataList;
import com.vape.data.streaming.swagger.v1.model.SensorDataList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin()
@AllArgsConstructor
@Slf4j
public class DataController implements DataApi {

    private final DataQueryService dataQueryService;
    private final DspDataPointModelMapper dspDataPointModelMapper;
    private final SensorDataPointModelMapper mapper;

    @Override
    public ResponseEntity<DspDataList> dataDspGet(String sensorId, Integer from, Integer to){
        List<DspDataPointModel> dspDataPointModelList = dataQueryService.getDspDataBySensorAndDateRange(sensorId, from, to);
        DspDataList dspDataList = dspDataPointModelMapper.toDspDataList(dspDataPointModelList);
        dspDataList.setFrom(from);
        dspDataList.setTo(to);
        dspDataList.setSensorId(sensorId);
        return new ResponseEntity<>(dspDataList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SensorDataList> dataSensorGet(String sensorId, Integer from, Integer to) {
        List<SensorDataPointModel> sensorDataPointModels = dataQueryService.getSensorDataByIdAndDateRange(sensorId, from, to);
        System.out.println(sensorDataPointModels.size());
        SensorDataList sensorDataList = mapper.toSensorDataList(sensorDataPointModels);
        sensorDataList.setFrom(from);
        sensorDataList.setTo(to);
        sensorDataList.setSensorId(sensorId);
        return new ResponseEntity<>(sensorDataList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DataStatus> dataStatusGet(String sensorId){
        DataStatus dataStats = new DataStatus()
                .sensorId(sensorId)
                .dsp(dataQueryService.getDspOrSensorDataStats(sensorId, KafkaTopic.DSP))
                .sensor(dataQueryService.getDspOrSensorDataStats(sensorId, KafkaTopic.SENSOR));
        return new ResponseEntity<>(dataStats, HttpStatus.OK);
    }
}
