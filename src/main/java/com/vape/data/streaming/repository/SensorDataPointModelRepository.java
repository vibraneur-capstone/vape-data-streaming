package com.vape.data.streaming.repository;

import com.vape.data.streaming.model.SensorDataPointModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SensorDataPointModelRepository extends MongoRepository<SensorDataPointModel, String> {

    @Query(value = "{sensorId: ?0, 'timestamp': {$gte: ?1, $lte:?2 }}", sort = "{'timestamp': 1}")
    List<SensorDataPointModel> findSensorDataByIdAndDateRange(String id, String from, String to);

    @Query(value = "{sensorId: ?0}", sort = "{'timestamp': 1}", fields = "{'timestamp': 1}")
    List<SensorDataPointModel> findAllTimestampsBySensorId(String sensorId);
}
