package com.heliozz10.qoschat.content.chat.message;

import com.heliozz10.qoschat.entity.Message;
import lombok.Getter;
import lombok.Setter;

/**
 * This is used to safely send a message from the server to the client. Trying to directly use the Message class
 * results in an error caused by infinite chat -> message -> chat -> message -> ... cycle
 */
@Getter
@Setter
public class MessageWrapper {
    private String senderName;
    private String contents;
    private String date;

    public MessageWrapper(Message message) {
        this.senderName = message.getSender().getUsername();
        this.contents = message.getContents();
        this.date = message.getDate();
    }
}
