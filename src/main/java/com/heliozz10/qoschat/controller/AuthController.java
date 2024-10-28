package com.heliozz10.qoschat.controller;

import com.heliozz10.qoschat.dto.UserDto;
import com.heliozz10.qoschat.entity.User;
import com.heliozz10.qoschat.repository.UserRepository;
import com.heliozz10.qoschat.security.AuthProvider;
import com.heliozz10.qoschat.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserRepository userRepo, UserService userService, AuthProvider authProvider) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/register")
    public String register() {
        return "register.html";
    }

    @PostMapping("/register")
    public String registerPost(UserDto userDto, HttpServletRequest req) throws ServletException {
        User user = userService.saveUserIfNotExists(userDto);
        if(user == null) {
            return "redirect:/register?error=already-exists";
        }
        req.login(user.getUsername(), userDto.getPassword());
        return "redirect:/chat";
    }
}
