package com.heliozz10.qoschat.controller;

import com.heliozz10.qoschat.content.chat.NewChatRequest;
import com.heliozz10.qoschat.content.chat.NewChatResponse;
import com.heliozz10.qoschat.content.chat.Sender;
import com.heliozz10.qoschat.content.chat.message.ClientChatMessage;
import com.heliozz10.qoschat.content.chat.message.ServerChatMessage;
import com.heliozz10.qoschat.entity.Chat;
import com.heliozz10.qoschat.entity.User;
import com.heliozz10.qoschat.repository.ChatRepository;
import com.heliozz10.qoschat.service.ChatService;
import com.heliozz10.qoschat.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.core.ApplicationFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
public class ChatController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    private final UserService userService;
    private final ChatRepository chatRepo;
    private final ChatService chatService;

    private final SimpMessagingTemplate template;

    private final SimpleDateFormat MESSAGE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public ChatController(UserService userService, ChatRepository chatRepo, ChatService chatService, SimpMessagingTemplate template) {
        this.userService = userService;
        this.chatRepo = chatRepo;
        this.chatService = chatService;
        this.template = template;
    }

    @PostMapping("/create-chat")
    public ResponseEntity<NewChatResponse> createChat(NewChatRequest request) {
        Chat chat = chatService.createChatAndSave(request.getName());
        return ResponseEntity.ok(new NewChatResponse(chat.getId()));
    }

    @MessageMapping("/send-message")
    public void handleChatMessage(ClientChatMessage message, Authentication auth) {
        User user = (User) auth.getPrincipal();
        template.convertAndSend("/topic/chat/" + user.getCurrentChat().getId(), new ServerChatMessage(new Sender(user.getUsername()), message.getContents(), MESSAGE_DATE_FORMAT.format(new Date())));
    }
}
