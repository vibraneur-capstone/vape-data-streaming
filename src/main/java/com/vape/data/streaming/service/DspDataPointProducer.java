package com.vape.data.streaming.service;

import com.vape.data.streaming.model.FFTDataPoint;
import com.vape.data.streaming.model.KafkaTopic;
import com.vape.data.streaming.utility.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DspDataPointProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private JsonMapper mapper;

    public void publishFFT(FFTDataPoint fft) {
        log.info(String.format("#### -> Publishing FFT data point for sensor data (id:: %s) ####", fft.getSensorDataPointId()));
        this.kafkaTemplate.send(KafkaTopic.FFT.toString(), mapper.toJson(fft));
    }

}
