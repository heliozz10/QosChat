package com.heliozz10.qoschat.websocket.interceptor;

import com.heliozz10.qoschat.entity.Chat;
import com.heliozz10.qoschat.repository.ChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatSubscriptionInterceptor implements ChannelInterceptor {
    private final ChatRepository chatRepo;

    public ChatSubscriptionInterceptor(ChatRepository chatRepo) {
        this.chatRepo = chatRepo;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            List<String> chatIdHolder = headerAccessor.getNativeHeader("chatId");
            if(chatIdHolder == null) {
                throw new IllegalArgumentException("chatId is required");
            }
            long chatId = Long.parseLong(chatIdHolder.get(0));
            Chat chat = chatRepo.findById(chatId).orElse(null);
            if(chat == null) {
                throw new IllegalArgumentException("chat not found");
            }
        }
        return message;
    }
}