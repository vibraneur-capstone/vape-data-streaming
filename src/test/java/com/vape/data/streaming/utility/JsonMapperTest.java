package com.vape.data.streaming.utility;
import com.vape.data.streaming.model.FFTDataPointModel;
import com.vape.data.streaming.model.SensorDataPointModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class JsonMapperTest {
    @InjectMocks
    private JsonMapper serviceToTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("should convert any object to json")
    void test_to_json_conversion() {
        // Arrange
        SensorDataPointModel objectOne = SensorDataPointModel.builder().sensorId("123").id("567").build();
        FFTDataPointModel objectTwo = FFTDataPointModel.builder().sensorDataPointId("567").build();
        String expectedJsonStringOne = "{\"id\":\"567\",\"sensorId\":\"123\",\"sensorHubId\":null,\"timestamp\":null,\"data\":null}";
        String expectedJsonStringTwo = "{\"id\":null,\"sensorDataPointId\":\"567\",\"imaginaryValue\":null,\"realValue\":null}";

        // Act
        String actualJsonStringOne = serviceToTest.toJson(objectOne);
        String actualJsonStringTwo = serviceToTest.toJson(objectTwo);

        // Assert
        assertAll("ensure OK",
                () -> assertEquals(expectedJsonStringOne, actualJsonStringOne),
                () -> assertEquals(expectedJsonStringTwo, actualJsonStringTwo)
        );
    }
}
