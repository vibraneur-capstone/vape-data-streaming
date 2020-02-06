package com.vape.data.streaming.mapper;

import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.swagger.v1.model.SensorData;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A DTO mapper that map all objects. Since we do not have much object as of now. All mapping methods are in one single file.
 * Will abstract them out if this mapper keeps getting bigger.
 */
public class DtoMapper {

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
