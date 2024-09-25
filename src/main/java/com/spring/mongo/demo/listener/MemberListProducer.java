package com.spring.mongo.demo.listener;

import com.spring.mongo.demo.entity.Member;
import com.spring.mongo.demo.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberListProducer {

    @Autowired
    private MemberRepository memberRepository;

    @Getter
    private List<Member> members;

    @EventListener
    public void onMemberListChanged(MemberRegisteredEvent event) {
        retrieveAllMembersOrderedByName();
    }

    @PostConstruct
    public void retrieveAllMembersOrderedByName() {
        members = memberRepository.findAllOrderedByName();
    }
}
