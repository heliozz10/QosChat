package com.heliozz10.qoschat.content.chat.message;

import com.heliozz10.qoschat.content.chat.Sender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Message sent to users participating in the chat
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServerChatMessage {
    private Sender sender;
    private String contents;
    /**
     * Time the message was sent in string format
     */
    private String date;
}
