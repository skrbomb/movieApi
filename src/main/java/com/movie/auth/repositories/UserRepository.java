package com.movie.auth.repositories;

import com.movie.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String username);

    @Modifying
    @Transactional
    @Query("update User u set u.password=?2 where u.email=?1")
    void  updateUserPassword(String email, String password);
}
