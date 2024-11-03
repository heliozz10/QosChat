package com.heliozz10.qoschat.content.chat.message;

import com.heliozz10.qoschat.entity.Message;
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
    private String senderName;
    private String contents;
    /**
     * The time the message was sent in string format
     */
    private String date;

    public ServerChatMessage(Message message) {
        this.senderName = message.getSender().getUsername();
        this.contents = message.getContents();
        this.date = message.getDate();
    }
}
