package com.vape.data.streaming.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.kafka.producer")
public class KafkaProducerConfig {
    private String clientId;
    private String bootstrapServers;
    private String bufferMemory;
    private String batchSize;
}
