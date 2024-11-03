package com.heliozz10.qoschat.config;

import com.heliozz10.qoschat.entity.Chat;
import com.heliozz10.qoschat.entity.User;
import com.heliozz10.qoschat.repository.ChatRepository;
import com.heliozz10.qoschat.repository.UserRepository;
import com.heliozz10.qoschat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import java.util.Objects;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);

    private final UserRepository userRepo;
    private final UserService userService;
    private final ChatRepository chatRepo;

    public WebSocketConfig(UserRepository userRepo, UserService userService, ChatRepository chatRepo) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.chatRepo = chatRepo;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app", "/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket");
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        //create and register the interceptor with an anonymous class
        //this interceptor rejects a subscription if a user tries to subscribe to a chat that doesn't exist
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                if(!accessor.getCommand().equals(StompCommand.SUBSCRIBE) || !accessor.getDestination().startsWith("/topic/chat/")) {
                    return message;
                }
                //get the user from the session
                User user = (User) ((UsernamePasswordAuthenticationToken) Objects.requireNonNull(accessor.getHeader("simpUser"))).getPrincipal();
                if(user == null) {
                    throw new MessageHandlingException(message, "User " + accessor.getLogin() + " not found");
                }
                //get the chat by id
                String chatIdRaw = accessor.getFirstNativeHeader("chatId");
                if(chatIdRaw == null) {
                    throw new MessageHandlingException(message, "Missing chatId header");
                }
                Long chatId = Long.parseLong(chatIdRaw);
                Chat chat = chatRepo.findById(chatId).orElse(null);
                if(chat == null) {
                    throw new IllegalArgumentException("Chat with id " + chatId + " not found");
                }
                user.setCurrentChat(chat);
                return message;
            }
        });
        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
    }
}
