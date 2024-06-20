package com.ssafy.s10p31s102be.admin.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class StompHandler implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 메시지 전송 전에 수행할 작업
        return message;
    }


    @EventListener
    public void handleWebSocketConnectionListener(SessionConnectedEvent e ){
        log.info("사용자 입장");
    }

    @EventListener
    public void handleWebSocketDisConnectionListener(SessionDisconnectEvent e ){
        log.info("사용자 퇴장");
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        // 메시지 전송 후에 수행할 작업
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        // 메시지 전송이 완료된 후에 수행할 작업
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        // 메시지 수신 전에 수행할 작업
        return true; // 메시지를 받을지 여부를 결정
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        // 메시지 수신 후에 수행할 작업
        return message;
    }
}
