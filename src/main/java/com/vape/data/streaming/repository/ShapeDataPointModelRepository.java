package com.vape.data.streaming.repository;

import com.vape.data.streaming.model.ShapeDataPointModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShapeDataPointModelRepository extends MongoRepository<ShapeDataPointModel, String> {
}
