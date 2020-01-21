package com.vape.data.streaming.repository;

import com.vape.data.streaming.model.KurtosisDataPoint;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KurtosisDataPointRepository extends MongoRepository<KurtosisDataPoint, String> {
}
