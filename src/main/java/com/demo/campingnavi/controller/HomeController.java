package com.demo.campingnavi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String goindex() {
        return "member/loginPage";
    }

    @RequestMapping("/main")
    public String gomain() {
        return "mainPage";
    }
}
