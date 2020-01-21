package com.vape.data.streaming.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.kafka.consumer")
public class KafkaConsumerConfig {
    private String bootstrapServers;
    private String groupId;
    private String autoOffsetReset;
//    private String heartbeatInterval;
}
