package com.heliozz10.qoschat.security;

import com.heliozz10.qoschat.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;
    private final AuthProvider authProvider;

    public SecurityConfig(UserService userService, AuthProvider authProvider) {
        this.userService = userService;
        this.authProvider = authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/login", "/register", "/css/**").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CustomCsrfTokenRequestHandler()))
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/chat", true)
                        .usernameParameter("username")
                        .passwordParameter("password"))
                .logout(logout -> logout
                        .logoutSuccessUrl("/login"))
                .userDetailsService(userService)
                .authenticationProvider(authProvider)
                .build();
    }
}
