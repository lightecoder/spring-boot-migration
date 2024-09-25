package com.spring.mongo.demo.controller;

import com.spring.mongo.demo.entity.Member;
import com.spring.mongo.demo.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberResourceRESTServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        memberRepository.deleteAll();
    }

    @Test
    public void shouldRegisterMemberSuccessfully() {
        // Arrange
        Member member = new Member();
        member.setName("Mark");
        member.setEmail("mark@example.com");
        member.setPhoneNumber("1234567890");

        // Act
        ResponseEntity<Member> response = restTemplate.postForEntity("http://localhost:" + port + "/api/members", member, Member.class);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Mark", response.getBody().getName());
        assertNotNull(response.getBody().getId());

        // Check in DB
        Optional<Member> savedMember = memberRepository.findById(response.getBody().getId());
        assertTrue(savedMember.isPresent());
        assertEquals("Mark", savedMember.get().getName());
    }

    @Test
    public void shouldReturnBadRequestForDuplicateEmail() {
        // Arrange
        Member existingMember = new Member();
        existingMember.setName("Mark");
        existingMember.setEmail("duplicate@example.com");
        existingMember.setPhoneNumber("9876543210");
        memberRepository.save(existingMember);

        Member newMember = new Member();
        newMember.setName("John Smith");
        newMember.setEmail("duplicate@example.com");
        newMember.setPhoneNumber("1234567890");

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/api/members", newMember, String.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Unique Email Violation"));
    }

    @Test
    public void shouldGetMemberById() {
        // Arrange
        Member member = new Member();
        member.setName("Mark");
        member.setEmail("mark@example.com");
        member.setPhoneNumber("1234567890");
        Member savedMember = memberRepository.save(member);

        // Act
        ResponseEntity<Member> response = restTemplate.getForEntity("http://localhost:" + port + "/api/members/" + savedMember.getId(), Member.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mark", response.getBody().getName());
    }

    @Test
    public void shouldReturnNotFoundForInvalidId() {
        // Act
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/members/invalid-id", String.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void shouldDeleteMemberById() {
        // Arrange
        Member member = new Member();
        member.setName("Mark");
        member.setEmail("mark@example.com");
        member.setPhoneNumber("1234567890");
        Member savedMember = memberRepository.save(member);

        // Act
        restTemplate.delete("http://localhost:" + port + "/api/members/" + savedMember.getId());

        // Assert
        assertFalse(memberRepository.findById(savedMember.getId()).isPresent());
    }
}
