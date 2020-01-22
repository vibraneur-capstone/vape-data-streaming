package com.vape.data.streaming.repository;

import com.vape.data.streaming.model.KurtosisDataPointModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KurtosisDataPointModelRepository extends MongoRepository<KurtosisDataPointModel, String> {
}
