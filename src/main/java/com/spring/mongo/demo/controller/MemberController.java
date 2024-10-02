package com.spring.mongo.demo.controller;

import com.spring.mongo.demo.entity.Member;
import com.spring.mongo.demo.service.MemberService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/members")
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new Member());

        // Fetch the list of members and add it to the model
        List<Member> members = memberService.findAllMembers();
        model.addAttribute("members", members);

        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute Member member, BindingResult result, Model model) {
        // Always ensure the member list is available in the model
        List<Member> members = memberService.findAllMembers();
        model.addAttribute("members", members);

        if (result.hasErrors()) {
            log.error("Validation errors occurred: {}", result.getAllErrors());
            model.addAttribute("errorMessage", "Please fix the errors in the form");
            return "register";
        }

        try {
            memberService.registerModel(member);
            model.addAttribute("message", "Registration successful!");

            // Update the member list after a successful registration
            members = memberService.findAllMembers();
            model.addAttribute("members", members);

        } catch (Exception e) {
            log.error("Failed to register member", e);
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "register";
    }
}
