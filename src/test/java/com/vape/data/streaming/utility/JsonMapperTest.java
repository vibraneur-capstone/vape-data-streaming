package com.vape.data.streaming.utility;
import com.fasterxml.jackson.core.JsonProcessingException;
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

        String expectedJsonStringOne = "{\"id\":\"567\",\"sensorId\":\"123\",\"sensorHubId\":null,\"timestamp\":null,\"raw\":null}";

        // Act
        String actualJsonStringOne = serviceToTest.toJson(objectOne);

        // Assert
        assertAll("ensure OK",
                () -> assertEquals(expectedJsonStringOne, actualJsonStringOne)
        );
    }

    @Test
    @DisplayName("should return null and not throw exception")
    void test_to_json_bad_boy() {
        // Arrange
        SensorDataPointModel badModel = null;

        // Act
        String actualJsonStringOne = serviceToTest.toJson(badModel);

        // Assert
        assertAll("ensure OK",
                () -> assertEquals("null", actualJsonStringOne)
        );
    }

    @Test
    @DisplayName("should convert to SensorDataPointModel object from json")
    void test_to_object_conversion() throws JsonProcessingException {
        // Arrange
        String stringOne = "{\"id\":\"567\",\"sensorId\":\"123\",\"sensorHubId\":null,\"timestamp\":\"2020-01-22T19:49:45.407517\",\"raw\":null}";

        SensorDataPointModel object = serviceToTest.toObject(stringOne, SensorDataPointModel.class);
        assertAll("ensure OK",
                () -> assertEquals(object.getId(), "567"));
    }
}
