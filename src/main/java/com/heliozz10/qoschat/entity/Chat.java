package com.heliozz10.qoschat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

//    @Column
//    private String password = null;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    public Chat(String name) {
        this.name = name;
    }
}
