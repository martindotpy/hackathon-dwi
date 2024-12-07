package xyz.cupscoffee.hackathondwi.auth.adapter.in.controller;

import org.springframework.web.bind.annotation.GetMapping;

import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.WebControllerAdapter;

@WebControllerAdapter
public class AuthViewController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }
}
