package com.heliozz10.qoschat.service;

import com.heliozz10.qoschat.dto.UserDto;
import com.heliozz10.qoschat.entity.Authority;
import com.heliozz10.qoschat.entity.User;
import com.heliozz10.qoschat.exception.AuthorityNotFoundException;
import com.heliozz10.qoschat.repository.AuthorityRepository;
import com.heliozz10.qoschat.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepo;
    private final AuthorityRepository authorityRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo,AuthorityRepository authorityRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUserPresent(String username) {
        return userRepo.findByUsername(username).isPresent();
    }

    public void ifUserExists(String username, Consumer<User> action) {
        userRepo.findByUsername(username).ifPresent(action);
    }

    public User createUserFromDto(UserDto userDto) {
        User user = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()));
        Authority userAuthority = authorityRepo.findByName("USER").orElse(null);
        if(userAuthority == null) {
            throw new AuthorityNotFoundException("Registration failed with a fatal error: USER authority not found");
        }
        user.addAuthority(userAuthority);
        return user;
    }

    public synchronized User saveUserIfNotExists(UserDto userDto) {
        if(!isUserPresent(userDto.getUsername())) {
            return userRepo.save(createUserFromDto(userDto));
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
