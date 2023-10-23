package com.example.randomcoffee.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    @GetMapping("/")
    public String mainPage(Model model) {
        String greeting = "This is main page";
        model.addAttribute("mainPage", greeting);
        return "main-page";
    }

    @GetMapping("/signIn")
    public String signIn(Model model) {
        model.addAttribute("signIn", "Войти");
        return "sign-in";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("regForm", "Форма регистрации");
        return "reg-form";
    }
}
