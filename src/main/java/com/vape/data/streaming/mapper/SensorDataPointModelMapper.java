package com.vape.data.streaming.mapper;

import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.swagger.v1.model.SensorData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SensorDataPointModelMapper {

    public SensorDataPointModel toSensorDataPointModel(SensorData sensorData) {
        List<Double> dataPoint = sensorData.getData().
                stream()
                .map(BigDecimal::doubleValue)
                .collect(Collectors.toList());

        return SensorDataPointModel
                .builder()
                .sensorId(sensorData.getSensorId())
                .data(dataPoint).build();
    }
}
