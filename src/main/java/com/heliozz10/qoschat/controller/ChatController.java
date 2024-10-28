package com.heliozz10.qoschat.controller;

import com.heliozz10.qoschat.content.chat.NewChatRequest;
import com.heliozz10.qoschat.content.chat.NewChatResponse;
import com.heliozz10.qoschat.content.chat.Sender;
import com.heliozz10.qoschat.content.chat.message.ClientChatMessage;
import com.heliozz10.qoschat.content.chat.message.ServerChatMessage;
import com.heliozz10.qoschat.entity.Chat;
import com.heliozz10.qoschat.service.ChatService;
import com.heliozz10.qoschat.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.core.ApplicationFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final ChatService chatService;

    private final SimpMessagingTemplate template;

    private final SimpleDateFormat MESSAGE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public ChatController(UserService userService, ChatService chatService, SimpMessagingTemplate template) {
        this.userService = userService;
        this.chatService = chatService;
        this.template = template;
    }

    @GetMapping("/chat-exists")
    @ResponseBody
    public Map<String, Boolean> checkIfChatExists(@RequestParam long id) {
        return Map.of("chatExists", chatService.isChatPresent(id));
    }

    @PostMapping("/create-chat")
    public ResponseEntity<NewChatResponse> createChat(NewChatRequest request) {
        LOGGER.info(request.getName());
        Chat chat = chatService.createChatAndSave(request.getName());
        return ResponseEntity.ok(new NewChatResponse(chat.getId()));
    }

    @MessageMapping("/send-message/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public ServerChatMessage handleChatMessage(ClientChatMessage message, Principal principal) {
        LOGGER.info(message.getContents());
        LOGGER.info(principal.getName());
        return new ServerChatMessage(new Sender(principal.getName()), message.getContents(), MESSAGE_DATE_FORMAT.format(new Date()));
    }
}
