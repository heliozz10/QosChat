package com.heliozz10.qoschat.repository;

import com.heliozz10.qoschat.entity.Authority;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends ListCrudRepository<Authority, Long> {
    Optional<Authority> findByName(String name);
}
