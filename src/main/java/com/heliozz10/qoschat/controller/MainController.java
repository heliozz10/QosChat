package com.heliozz10.qoschat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat.html";
    }
}
