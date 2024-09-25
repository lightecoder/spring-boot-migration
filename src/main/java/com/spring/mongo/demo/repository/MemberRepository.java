package com.spring.mongo.demo.repository;

import com.spring.mongo.demo.entity.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member, String> {

    @Query(value = "{}", sort = "{ 'name' : 1 }")
    List<Member> findAllOrderedByName();

    Optional<Member> findByEmail(String email);
}
