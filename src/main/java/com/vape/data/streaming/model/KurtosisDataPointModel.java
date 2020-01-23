package com.vape.data.streaming.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection="kurtosis")
public class KurtosisDataPointModel {
    @Id
    private String id;
    private String sensorDataPointId;
    private String timestamp;
    private Double data;
}
