package com.blog.blog.repository;

import com.blog.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    List<User> findAll();
    User findByEmail(String email);
    User findUserByToken(String token);
    User findUserByEmailAndPassword(String email, String password);


    @Modifying
    @Query("update User u  set u.active=true where u.token=:token")
    void activateUser(@Param("token") String token);

    @Modifying
    @Query("update User u set u.loginDate=current_date where u.email=:email")
    void updateLoginDate(@Param("email") String email);

    @Modifying
    @Query("update User u set u.active=false where u.loginDate<:date")
    void deactivateUser(@Param("date")LocalDate date);


    @Modifying
    @Query("update User u set u.active=false where u.email<:email")
    void deactivateUser2(@Param("email")String email);


}
