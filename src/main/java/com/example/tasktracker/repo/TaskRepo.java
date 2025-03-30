package com.example.tasktracker.repo;

import com.example.tasktracker.model.entity.Task;
import com.example.tasktracker.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    Page<Task> findAllByAuthor(User author, Pageable pageable);

    Page<Task> findAllByExecutor(User executor, Pageable pageable);

    Optional<Task> findById(long taskId);

    Optional<Task> findByExecutorAndId(User executor, long taskId);



}
