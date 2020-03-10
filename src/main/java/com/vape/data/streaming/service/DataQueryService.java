package com.vape.data.streaming.service;

import com.vape.data.streaming.model.DspDataPointModel;
import com.vape.data.streaming.model.KafkaTopic;
import com.vape.data.streaming.model.SensorDataPointModel;
import com.vape.data.streaming.repository.DspDataPointModelRepository;
import com.vape.data.streaming.repository.SensorDataPointModelRepository;
import com.vape.data.streaming.swagger.v1.model.Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

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

    public Status getDspOrSensorDataStats(String sensorId, KafkaTopic type) {
        List<String> timestampList = new ArrayList<>();
        if (type == KafkaTopic.DSP) {
            timestampList = emptyIfNull(dspDataPointModelRepository.findAllTimestampsBySensorId(sensorId))
                    .stream()
                    .map(DspDataPointModel::getTimestamp)
                    .collect(Collectors.toList());
        } else if (type == KafkaTopic.SENSOR) {
            timestampList = emptyIfNull(sensorDataPointModelRepository.findAllTimestampsBySensorId(sensorId))
                    .stream()
                    .map(SensorDataPointModel::getTimestamp)
                    .collect(Collectors.toList());
        }
        return updateDspDataStatus(timestampList);
    }

    private Status updateDspDataStatus(List<String> timestampList) {
        int dataSize = timestampList.size();
        Status dataStatus = new Status().count(dataSize);
        if (dataSize != 0) {
            dataStatus.setEarliestTimestamp(timestampList.get(0));
            dataStatus.setLatestTimestamp(timestampList.get(dataSize - 1));
        }
        return dataStatus;
    }

    private String toLocalDateTimeString(Integer epoch) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault()).toString();
    }
}
