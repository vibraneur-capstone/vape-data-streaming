package com.vape.data.streaming.repository;

import com.vape.data.streaming.model.RMSDataPointModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RMSDataPointModelRepository extends MongoRepository<RMSDataPointModel, String> {
}
