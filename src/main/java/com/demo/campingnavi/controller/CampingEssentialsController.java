package com.demo.campingnavi.controller;

import com.demo.campingnavi.model.CampItem;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CampingEssentialsController {

    private List<CampItem> products;

    public CampingEssentialsController() {
        products = new ArrayList<>();

        products.add(new CampItem("텐트", "튼튼하고 설치가 쉬운 텐트.", "https://example.com/tent.jpg", "https://shopping.com/tent"));
        products.add(new CampItem("침낭", "따뜻하고 편안한 침낭.", "https://example.com/sleepingbag.jpg", "https://shopping.com/sleepingbag"));
        products.add(new CampItem("캠핑 의자", "휴식을 위한 편안한 의자.", "https://example.com/chair.jpg", "https://shopping.com/chair"));
        products.add(new CampItem("랜턴", "야간 조명을 위한 랜턴.", "https://example.com/lantern.jpg", "https://shopping.com/lantern"));
        products.add(new CampItem("아이스 쿨러", "음식과 음료를 신선하게 유지.", "https://example.com/coolerbox.jpg", "https://shopping.com/coolerbox"));
    }

    @GetMapping("/camp/essentials")
    public ModelAndView showCampingEssentials(Model model) {
        model.addAttribute("products", products);
        return new ModelAndView("campingEssentials/campingEssentials");
    }

}
