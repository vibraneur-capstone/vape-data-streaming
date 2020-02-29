package com.vape.data.streaming.config;

import com.vape.data.streaming.model.DspTopic;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@Slf4j
@Setter(AccessLevel.PUBLIC)
@ConfigurationProperties("com.vape.dsp.integration")
public class DspEngineConfig {
    private String uri;
    private String kurtosis;
    private String fft;
    private String rms;
    private String crest;
    private String shape;
    private String apiKey;

    public String getUriByDspTopic(DspTopic topic) {
        switch (topic) {
            case FFT: return fft;
            case RMS: return rms;
            case CREST: return crest;
            case SHAPE: return shape;
            case KURTOSIS: return kurtosis;
            default: {
                log.error("Invalid DSP Topic");
                return "";
            }
        }
    }
}
