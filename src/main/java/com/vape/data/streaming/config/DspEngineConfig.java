package com.vape.data.streaming.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("com.vape.dsp.integration")
public class DspEngineConfig {
    private String uri;
    private String kurtosis;
    private String fft;
    private String rms;
    private String crest;
    private String shape;
}
