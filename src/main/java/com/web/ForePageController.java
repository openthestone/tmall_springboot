package com.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ForePageController {
    @GetMapping(value = "/")
    public String index() {
        return "redirect:home";
    }

    @GetMapping(value = "/home")
    public String home() {
        return "fore/index";
    }

    @GetMapping(value = "/register")
    public String register() {
        return "fore/register";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "fore/login";
    }

    @GetMapping(value = "/registerSuccess")
    public String registerSuccess() {
        return "fore/registerSuccess";
    }

    @GetMapping("/foreLogout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:home";
    }
    @GetMapping(value = "/standard")
    public String standard() {
        return "fore/square";
    }
    @GetMapping(value = "/non_standard")
    public String non_standard() {
        return "fore/nonStandard";
    }
    @GetMapping(value = "/api")
    public String api() {
        return "fore/api";
    }
    @GetMapping(value = "/gen")
    public String gen() {
        return "fore/gen";
    }
    @PostMapping(value="/search")
    public String searchResult(){
        return "fore/gen";
    }
}
