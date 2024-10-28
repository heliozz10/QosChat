package com.heliozz10.qoschat.repository;

import com.heliozz10.qoschat.entity.Chat;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends ListCrudRepository<Chat, Long> {

}
