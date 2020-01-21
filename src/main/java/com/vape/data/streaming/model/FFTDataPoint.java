package com.vape.data.streaming.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
public class FFTDataPoint {
    @Id
    private String id;
    private String sensorDataPointId;
    private List<Double> imaginaryValue;
    private List<Double> realValue;
}
