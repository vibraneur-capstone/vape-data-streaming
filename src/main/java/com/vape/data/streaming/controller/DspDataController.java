package com.vape.data.streaming.controller;

import com.vape.data.streaming.mapper.DspDataPointModelMapper;
import com.vape.data.streaming.model.DspDataPointModel;
import com.vape.data.streaming.service.DspDataQueryService;
import com.vape.data.streaming.swagger.v1.api.DspApi;
import com.vape.data.streaming.swagger.v1.model.DataStatus;
import com.vape.data.streaming.swagger.v1.model.DspDataList;
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
public class DspDataController implements DspApi{

    private final DspDataQueryService dspDataQueryService;
    private final DspDataPointModelMapper dspDataPointModelMapper;

    @Override
    public ResponseEntity<DspDataList> dspDataGet(String sensorId, Integer from, Integer to){
        List<DspDataPointModel> dspDataPointModelList = dspDataQueryService.getDataBySensorAndDateRange(sensorId, from, to);
        DspDataList dspDataList = dspDataPointModelMapper.toDspDataList(dspDataPointModelList);
        dspDataList.setFrom(from);
        dspDataList.setTo(to);
        dspDataList.setSensorId(sensorId);
        return new ResponseEntity<>(dspDataList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DataStatus> dspDataStatusGet(String sensorId,Integer from,Integer to){
        return new ResponseEntity<>(new DataStatus(), HttpStatus.OK);
    }
}
