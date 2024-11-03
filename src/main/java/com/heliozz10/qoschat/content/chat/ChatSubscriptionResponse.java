package com.heliozz10.qoschat.content.chat;

import com.heliozz10.qoschat.content.chat.message.ServerChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ChatSubscriptionResponse {
    private String name;
    private List<ServerChatMessage> messages;
}
