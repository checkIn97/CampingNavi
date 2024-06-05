package com.demo.campingnavi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/camp")
public class CampController {

    @RequestMapping("/index")
    public String index() {
        return "loginPage";
    }

    @RequestMapping("/search")
    public String search() {
        return "search/searchPage";
    }
}
