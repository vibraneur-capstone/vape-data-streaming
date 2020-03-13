package com.vape.data.streaming.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Setter(AccessLevel.PUBLIC)
@Document(collection="sensor")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SensorDataPointModel {
    @Id
    private String id;
    private String sensorId;
    private String sensorHubId;
    private String timestamp;
    private List<Double> data;
}
