package com.heliozz10.qoschat.service;

import com.heliozz10.qoschat.entity.Chat;
import com.heliozz10.qoschat.repository.ChatRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final ChatRepository chatRepo;

    public ChatService(ChatRepository chatRepo) {
        this.chatRepo = chatRepo;
    }

    public boolean isChatPresent(Long id) {
        return chatRepo.findById(id).isPresent();
    }

    public Chat createChat(String name) {
        return new Chat(name);
    }

    public Chat createChatAndSave(String name) {
        return chatRepo.save(createChat(name));
    }
}
