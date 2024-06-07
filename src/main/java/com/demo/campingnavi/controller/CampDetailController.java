package com.demo.campingnavi.controller;

import com.demo.campingnavi.model.ApiResponse;
import com.demo.campingnavi.service.CampDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/camp/detail")

public class CampDetailController {

    @Autowired
    private CampDetailService campDetailService;

    @GetMapping("/go")
    public String go() {
        return "CampDetail/campDetail";
    }

    @GetMapping("/")
    public String campDetailView(@RequestParam("mapX") String mapX, @RequestParam("mapY") String mapY, Model model) {

        List<ApiResponse.Item> itemList = campDetailService.DataFromApi(mapX, mapY);
        model.addAttribute("item", itemList);

        return "CampDetail/campDetail";
    }


}
