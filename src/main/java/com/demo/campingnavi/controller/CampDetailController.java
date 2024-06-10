package com.demo.campingnavi.controller;

import com.demo.campingnavi.model.ApiImageResponse;
import com.demo.campingnavi.model.ApiResponse;
import com.demo.campingnavi.service.CampDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String campDetailView(@RequestParam("mapX") String mapX, @RequestParam("mapY") String mapY,@RequestParam("contentId") String contentId, @RequestParam("cseq") int cseq, Model model) {

        List<ApiResponse.Item> itemList = campDetailService.DataFromApi(mapX, mapY);
        List<ApiImageResponse.Item> imageList = campDetailService.DataFromApiImage((contentId));
        model.addAttribute("camps", itemList);
        model.addAttribute("imageUrls", imageList);

        return "CampDetail/campDetail";
    }


}
