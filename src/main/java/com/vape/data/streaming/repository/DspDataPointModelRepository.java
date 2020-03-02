package com.vape.data.streaming.repository;

import com.vape.data.streaming.model.DspDataPointModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DspDataPointModelRepository extends MongoRepository<DspDataPointModel, String> {

    @Query(value = "{sensorId: ?0, 'timestamp': {$gte: ?1, $lte:?2 }}", sort = "{'timestamp': 1}")
    List<DspDataPointModel> findDspDataBySensorAndDateRange(String id, String from, String to);

}
