package com.vape.data.streaming.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Setter(AccessLevel.PACKAGE)
@Document(collection="sensor")
public class SensorDataPoint {
    @Id
    private String id;
    private String sensorId;
    private String sensorHubId;
    private String timestamp;
    private List<Double> data;
}
