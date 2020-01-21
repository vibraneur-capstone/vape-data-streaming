package com.vape.data.streaming.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Setter(AccessLevel.PUBLIC)
@Document(collection="sensor")
public class SensorDataPointModel {
    @Id
    private String id;
    private String sensorId;
    private String sensorHubId;
    private LocalDateTime timestamp;
    private List<Double> data;
}
