package com.vape.data.streaming.mapper;

import com.vape.data.streaming.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ModelMapper {

    public ShapeDataPointModel constructShapeDataPointModel(SensorDataPointModel dataPointModel, BigDecimal computedData) {
        return ShapeDataPointModel.builder()
                .sensorDataPointId(dataPointModel.getId())
                .timestamp(LocalDateTime.now().toString())
                .data(computedData.doubleValue())
                .build();
    }

    public CrestDataPointModel constructCrestDataPointModel(SensorDataPointModel dataPointModel, BigDecimal computedData) {
        return CrestDataPointModel.builder()
                .sensorDataPointId(dataPointModel.getId())
                .timestamp(LocalDateTime.now().toString())
                .data(computedData.doubleValue())
                .build();
    }

    public KurtosisDataPointModel constructKurtosisDataPointModel(SensorDataPointModel dataPointModel, BigDecimal computedData) {
        return KurtosisDataPointModel.builder()
                .sensorDataPointId(dataPointModel.getId())
                .timestamp(LocalDateTime.now().toString())
                .data(computedData.doubleValue())
                .build();
    }

    public RMSDataPointModel constructRMSDataPointModel(SensorDataPointModel dataPointModel, BigDecimal computedData) {
        return RMSDataPointModel.builder()
                .sensorDataPointId(dataPointModel.getId())
                .timestamp(LocalDateTime.now().toString())
                .data(computedData.doubleValue())
                .build();
    }
}
