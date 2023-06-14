package com.clone.todomate.repository;

import com.clone.todomate.entity.User;
import com.clone.todomate.entity.UserSec;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UserSecRepository extends JpaRepository<UserSec, String> {

//    @EntityGraph(attributePaths = "user")
//    @Query("select us from UserSec  us left join User u on us.user = u where u.id=:id")
//    Optional<UserSec> getUserSecWithId(Long id);
}
