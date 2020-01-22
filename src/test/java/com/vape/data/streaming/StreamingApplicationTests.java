package com.vape.data.streaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
        basePackages = {"com.vape", "com.vape.data.streaming"}
)
public class StreamingApplicationTests {

    public static void main(String args[]) {
        SpringApplication.run(StreamingApplication.class, args);
    }

}
