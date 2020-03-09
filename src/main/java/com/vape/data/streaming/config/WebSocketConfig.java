package com.vape.data.streaming.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final static String CONTEXT_PATH = "/websocket";
    private final static String STOMP_END_POINTS = CONTEXT_PATH;
    private final static String ALLOWED_ORIGINS = "*";
    public final static String API_DESTINATION_PREFIX = CONTEXT_PATH + "/app";
    public final static String APP_DESTINATION_PREFIX = CONTEXT_PATH + "/message";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(STOMP_END_POINTS)
                .setAllowedOrigins(ALLOWED_ORIGINS).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(APP_DESTINATION_PREFIX);
        config.setApplicationDestinationPrefixes(API_DESTINATION_PREFIX);
    }

}
