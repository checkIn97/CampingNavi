package com.demo.campingnavi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/index")
    public String index() {
        return "loginPage";
    }

    @RequestMapping("/main")
    public String main() {
        return "mainPage";
    }
}
