package com.example.demo.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Long> {
   @Query("select * from User u where u.username = :username")
   User findByUsername(@Param("username") String username);

   @Query("select * from User u where u.emailAddress = :email")
   User findByEmailAddress(@Param("email") String email);
}
