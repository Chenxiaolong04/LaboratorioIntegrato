package com.immobiliaris.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/admin")
    public String admin() {
        return "admin"; // templates/admin.html
    }

    @GetMapping("/agent")
    public String agent() {
        return "agent"; // templates/agent.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }
}
