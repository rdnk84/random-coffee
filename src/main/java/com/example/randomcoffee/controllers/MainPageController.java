package com.example.randomcoffee.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {


    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("mainPage", "This is main page!");
        return "main-page";
    }

    @GetMapping("/signIn")
    public String signIn(Model model) {
        model.addAttribute("signIn", "Войти");
        return "sign-in";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("regForm", "Зарегистрироваться");
        return "reg-form";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("about", "О проекте");
        return "about";
    }
}
