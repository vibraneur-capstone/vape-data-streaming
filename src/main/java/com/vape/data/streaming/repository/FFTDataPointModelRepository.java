package com.vape.data.streaming.repository;

import com.vape.data.streaming.model.FFTDataPointModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FFTDataPointModelRepository extends MongoRepository<FFTDataPointModel, String> {
}
