package com.spring.mongo.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MemberEventListener {

    private static final Logger log = LoggerFactory.getLogger(MemberEventListener.class);

    @EventListener
    public void handleMemberRegisteredEvent(MemberRegisteredEvent event) {
        log.info("New member registered: {}", event.getMember().getName());
    }
}
