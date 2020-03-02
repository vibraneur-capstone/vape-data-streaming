package com.vape.data.streaming.service;

import com.vape.data.streaming.model.DspDataPointModel;
import com.vape.data.streaming.repository.DspDataPointModelRepository;
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
public class DspDataQueryService {

    private final DspDataPointModelRepository dspDataPointModelRepository;

    public List<DspDataPointModel> getDataBySensorAndDateRange(String sensorId, Integer from, Integer to) {
        String begin = toLocalDateTimeString(from);
        String end = toLocalDateTimeString(to);
        return dspDataPointModelRepository.findDspDataBySensorAndDateRange(sensorId, begin, end);
    }

    private String toLocalDateTimeString(Integer epoch) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault()).toString();
    }
}
