package com.demo.campingnavi.controller;

import com.demo.campingnavi.service.MailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private int number;

    // 인증 이메일 전송
    @PostMapping("/mailSend")
    public HashMap<String, Object> mailSend(String mail, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            number = mailService.sendMail(mail);
            String num = String.valueOf(number);
            System.out.println("num: " + num);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
            session.setAttribute("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

    // 인증번호 일치여부 확인
    @GetMapping("/mailCheck")
    public ResponseEntity<?> mailCheck(@RequestParam String userNumber, HttpSession session) {
        String strNumber = (String)session.getAttribute("number");
        number = Integer.parseInt(strNumber);
        boolean isMatch = userNumber.equals(String.valueOf(number));
        System.out.println("userNumber: " + userNumber);
        System.out.println("isMatch: " + isMatch);
        return ResponseEntity.ok(isMatch);
    }
}
