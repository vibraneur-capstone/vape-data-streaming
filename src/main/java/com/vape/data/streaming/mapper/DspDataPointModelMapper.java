package com.vape.data.streaming.mapper;

import com.vape.data.streaming.model.DspDataPointModel;
import com.vape.data.streaming.swagger.v1.model.DspDataList;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DspDataPointModelMapper {

    public DspDataList toDspDataList(List<DspDataPointModel> dspDataPointModelList) {
        DspDataList dspDataList = new DspDataList().fft(new ArrayList<>());
        dspDataPointModelList.forEach(model -> updateByDspDataPointModel(dspDataList, model));
        dspDataList.setCount(dspDataPointModelList.size());
        return dspDataList;
    }

    private void updateByDspDataPointModel(DspDataList dspDataList, DspDataPointModel dspDataPointModel) {
        if(dspDataPointModel.getCrest() != null){
            dspDataList.addCrestItem(new BigDecimal(dspDataPointModel.getCrest()));
        }
        if(dspDataPointModel.getKurtosis() != null){
            dspDataList.addKurtosisItem(new BigDecimal(dspDataPointModel.getKurtosis()));
        }
        if(dspDataPointModel.getRms() != null){
            dspDataList.addRmsItem(new BigDecimal(dspDataPointModel.getRms()));
        }
        if(dspDataPointModel.getShape() != null){
            dspDataList.addShapeItem(new BigDecimal(dspDataPointModel.getShape()));
        }
        if(dspDataPointModel.getFft() != null){
            dspDataList.getFft().addAll(dspDataPointModel.getFft().stream().map(BigDecimal::new).collect(Collectors.toList()));
        }
        if(dspDataPointModel.getSensorDataPointId() != null){
            dspDataList.addSensorDataPointIdListItem(dspDataPointModel.getSensorDataPointId());
        }
    }
}
