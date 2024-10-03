package com.heliozz10.qoschat.controller;

import com.heliozz10.qoschat.content.chat.Sender;
import com.heliozz10.qoschat.content.chat.message.ClientChatMessage;
import com.heliozz10.qoschat.content.chat.message.ServerChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {
    private final SimpleDateFormat MESSAGE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("/send-message/{chatId}")
    @SendTo("/topic/chats/{chatId}")
    public ServerChatMessage handleChatMessage(ClientChatMessage message, @DestinationVariable String chatId, Principal principal) {
        return new ServerChatMessage(chatId, new Sender(principal.getName()), message.getContents(), MESSAGE_DATE_FORMAT.format(new Date()));
    }
}
