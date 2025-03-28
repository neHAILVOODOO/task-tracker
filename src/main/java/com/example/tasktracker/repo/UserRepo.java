package com.example.tasktracker.repo;

import com.example.tasktracker.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findUserById(long id);
    User findUserByEmail(String email);

}