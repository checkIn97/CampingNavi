package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.dto.CustomOauth2UserDetails;
import com.demo.campingnavi.dto.CustomSecurityUserDetails;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@RequiredArgsConstructor
@Controller
public class HomeController {
    @RequestMapping("/")
    public String goindex() {
        return "member/loginPage";
    }

    @RequestMapping("/main")
    public String gomain(Model model) {
        return "mainPage";
    }
}
