package com.heliozz10.qoschat.repository;

import com.heliozz10.qoschat.entity.Chat;
import com.heliozz10.qoschat.entity.Message;
import com.heliozz10.qoschat.entity.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends ListCrudRepository<Message, Long> {
    List<Message> findBySender(User user);
    List<Message> findByChat(Chat chat);
}
