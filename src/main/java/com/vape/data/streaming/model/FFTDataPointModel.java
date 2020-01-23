package com.vape.data.streaming.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection="fft")
public class FFTDataPointModel {
    @Id
    private String id;
    private String sensorDataPointId;
    private List<Double> imaginaryValue;
    private List<Double> realValue;
    private String timestamp;
}
