package com.vape.data.streaming.service;

import com.vape.data.streaming.model.DspDataPointModel;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.repository.DspDataPointModelRepository;
import com.vape.data.streaming.repository.SensorDataPointModelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DataQueryService {

    private final DspDataPointModelRepository dspDataPointModelRepository;

    private final SensorDataPointModelRepository sensorDataPointModelRepository;

    public List<DspDataPointModel> getDspDataBySensorAndDateRange(String sensorId, Integer from, Integer to) {
        String begin = toLocalDateTimeString(from);
        String end = toLocalDateTimeString(to);
        return dspDataPointModelRepository.findDspDataBySensorAndDateRange(sensorId, begin, end);
    }

    public List<SensorDataPointModel> getSensorDataByIdAndDateRange(String sensorId, Integer from, Integer to) {
        String begin = toLocalDateTimeString(from);
        String end = toLocalDateTimeString(to);
        return sensorDataPointModelRepository.findSensorDataByIdAndDateRange(sensorId, begin, end);
    }

    private String toLocalDateTimeString(Integer epoch) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault()).toString();
    }
}
