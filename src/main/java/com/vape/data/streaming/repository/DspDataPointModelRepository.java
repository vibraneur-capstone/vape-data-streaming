package com.vape.data.streaming.repository;

import com.vape.data.streaming.model.DspDataPointModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DspDataPointModelRepository extends MongoRepository<DspDataPointModel, String> {
}
