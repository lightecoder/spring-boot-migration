package com.spring.mongo.demo.service;

import com.spring.mongo.demo.entity.Member;
import com.spring.mongo.demo.exception.EntityNotFoundException;
import com.spring.mongo.demo.listener.MemberRegisteredEvent;
import com.spring.mongo.demo.repository.MemberRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;


@Service
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public ResponseEntity<?> register(Member member) {
        try {
            validateMember(member);
        } catch (ConstraintViolationException e) {
            return createViolationResponse(e.getConstraintViolations());
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(responseObj);
        }

        log.info("Registering {}", member.getName());
        Member savedMember = memberRepository.save(member);
        eventPublisher.publishEvent(new MemberRegisteredEvent(this, member));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedMember.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedMember);
    }

    public void registerModel(Member member) {
        validateMember(member);
        log.info("Registering new member {}", member.getName());
        memberRepository.save(member);
        eventPublisher.publishEvent(new MemberRegisteredEvent(this, member));
    }

    private void validateMember(Member member) throws ValidationException {
        if (emailAlreadyExists(member.getEmail())) {
            throw new ValidationException("Unique Email Violation");
        }
    }

    private boolean emailAlreadyExists(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    private ResponseEntity<?> createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.debug("Validation completed. Violations found: {}", violations.size());

        Map<String, String> responseObj = new HashMap<>();
        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return ResponseEntity.badRequest().body(responseObj);
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAllOrderedByName();
    }

    public ResponseEntity<Member> lookupMemberById(String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + id));
        return ResponseEntity.ok(member);
    }

    public void deleteUser(String id) {
        memberRepository.deleteById(id);
    }
}
