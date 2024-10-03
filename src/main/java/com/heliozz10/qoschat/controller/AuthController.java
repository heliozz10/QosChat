package com.heliozz10.qoschat.controller;

import com.heliozz10.qoschat.security.PasswordlessAuthProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private PasswordlessAuthProvider authProvider;

    public AuthController(PasswordlessAuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    @GetMapping("/set-name")
    public String setName(HttpServletRequest request, @RequestParam String name) {
        SecurityContext sc = SecurityContextHolder.getContext();
        final UserDetails user = new User(name, "", AuthorityUtils.createAuthorityList("ROLE_USER"));
        sc.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
        return "redirect:/";
    }
}
