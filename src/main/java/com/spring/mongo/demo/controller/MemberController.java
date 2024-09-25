package com.spring.mongo.demo.controller;

import com.spring.mongo.demo.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.spring.mongo.demo.service.MemberRegistrationService;

@Controller
@RequestMapping("/members")
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberRegistrationService memberService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Member member, Model model) {
        try {
            memberService.registerModel(member);
            model.addAttribute("message", "Registration successful!");
        } catch (Exception e) {
            log.error("Fail to register user", e);
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "register";
    }

}
