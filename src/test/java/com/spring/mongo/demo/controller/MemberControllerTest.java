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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

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
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name", "Mark");
        form.add("email", "mark@example.com");
        form.add("phoneNumber", "1234567890");

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/members/register", form, String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Registration successful!"));
        assertTrue(response.getBody().contains("value=\"Mark\""));
    }

    @Test
    public void shouldReturnBadRequestForDuplicateEmail() {
        // Arrange
        Member existingMember = new Member();
        existingMember.setName("Mark");
        existingMember.setEmail("mark@example.com");
        existingMember.setPhoneNumber("9876543210");
        memberRepository.save(existingMember);

        MultiValueMap<String, String> newMemberWithDuplicatedEmail = new LinkedMultiValueMap<>();
        newMemberWithDuplicatedEmail.add("name", "Smith");
        newMemberWithDuplicatedEmail.add("email", "mark@example.com");
        newMemberWithDuplicatedEmail.add("phoneNumber", "1234567890");

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/members/register", newMemberWithDuplicatedEmail, String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Unique Email Violation"));
    }

    @Test
    public void shouldShowRegistrationForm() {
        // Act
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/members/register", String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Member Registration"));
    }

}
