package com.vape.data.streaming.repository;

import com.vape.data.streaming.model.SensorDataPointModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SensorDataPointRepository extends MongoRepository<SensorDataPointModel, String> {
}
