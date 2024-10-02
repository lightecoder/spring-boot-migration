package com.spring.mongo.demo.controller;

import com.spring.mongo.demo.entity.Member;
import com.spring.mongo.demo.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberResourceRESTService {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public List<Member> listAllMembers() {
        return memberService.findAllMembers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> lookupMemberById(@PathVariable String id) {
        return memberService.lookupMemberById(id);
    }

    @PostMapping
    public ResponseEntity<?> createMember(@Valid @RequestBody Member member) {
        return memberService.register(member);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        memberService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
