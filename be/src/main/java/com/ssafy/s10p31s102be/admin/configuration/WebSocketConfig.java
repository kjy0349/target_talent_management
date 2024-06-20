package com.ssafy.s10p31s102be.admin.configuration;

import com.ssafy.s10p31s102be.admin.handler.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/all-notification");
        registry.setApplicationDestinationPrefixes("/send");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
//                .setAllowedOriginPatterns("*")
//                .setAllowedOrigins("*")
                .setAllowedOrigins("http://localhost:5173", "http://localhost:3000", "http://k10s102.p.ssafy.io:3000")
                .withSockJS();
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // stompHandler를 인터셉터로 등록하여 STOMP 메시지 핸들링을 수행
        registration.interceptors(stompHandler);
    }

    // STOMP에서 64KB 이상의 데이터 전송을 못하는 문제 해결
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(160 * 64 * 1024);
        registry.setSendTimeLimit(100 * 10000);
        registry.setSendBufferSizeLimit(3 * 512 * 1024);
    }
}
