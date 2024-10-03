package com.heliozz10.qoschat.content.chat.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request from a client to send a message.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientChatMessage {
    /**
     * Chat UUID represented in string format
     */
    private String chatId;
    private String contents;
}
