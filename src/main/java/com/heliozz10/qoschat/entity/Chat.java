package com.heliozz10.qoschat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
@Table(name = "chat")
public class Chat {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    //TODO: add password
//    @Column
//    private String password = null;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    private List<Message> messages;

    public Chat(String name) {
        this.name = name;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addMessages(Collection<Message> messages) {
        this.messages.addAll(messages);
    }
}
