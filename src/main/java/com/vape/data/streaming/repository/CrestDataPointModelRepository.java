package com.vape.data.streaming.repository;

import com.vape.data.streaming.model.CrestDataPointModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CrestDataPointModelRepository extends MongoRepository<CrestDataPointModel, String> {
}
