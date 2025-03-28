package com.example.tasktracker.repo;

import com.example.tasktracker.model.entity.Task;
import com.example.tasktracker.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    List<Task> findAllByUser(User user);

    Task findByUserAndId(User user, long id);

    Task findById(long taskId);



}
