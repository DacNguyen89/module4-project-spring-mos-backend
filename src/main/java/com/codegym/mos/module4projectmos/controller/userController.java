package com.codegym.mos.module4projectmos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class userController {
    @GetMapping
    public ModelAndView user() {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }
}
