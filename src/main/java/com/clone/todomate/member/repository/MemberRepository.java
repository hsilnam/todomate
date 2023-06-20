package com.clone.todomate.member.repository;

import com.clone.todomate.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByEmailAndPw(String email, String pw);

    Optional<Member> findByEmail(String email);

    //    void deleteByEmailContaining(String email);
//
//
//    @Modifying
//    @Query("DELETE FROM User u where u.email like %:email%") // jpql
////    @Query(value = "delete from User where email like %:email%", nativeQuery=true)
//    void deleteByEmailContainingCustom(String email);
}
