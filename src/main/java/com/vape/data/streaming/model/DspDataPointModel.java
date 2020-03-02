package com.vape.data.streaming.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Setter(AccessLevel.PUBLIC)
@Document(collection="dsp")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DspDataPointModel {
    @Id
    private String id;
    private String sensorDataPointId;
    private String sensorId;
    private String timestamp;
    private Double rms;
    private Double kurtosis;
    private Double shape;
    private Double crest;
    private List<Double> fft;
}
