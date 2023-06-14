package com.clone.todomate.repository;

import com.clone.todomate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmailAndPw(String email, String pw);

//    void deleteByEmailContaining(String email);
//
//
//    @Modifying
//    @Query("DELETE FROM User u where u.email like %:email%") // jpql
////    @Query(value = "delete from User where email like %:email%", nativeQuery=true)
//    void deleteByEmailContainingCustom(String email);
}
