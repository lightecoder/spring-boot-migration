package com.spring.mongo.demo.listener;

import com.spring.mongo.demo.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberRegisteredEvent extends ApplicationEvent {
    private final Member member;

    public MemberRegisteredEvent(Object source, Member member) {
        super(source);
        this.member = member;
    }

}
