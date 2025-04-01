package com.example.tasktracker.repo;

import com.example.tasktracker.model.entity.Comment;
import com.example.tasktracker.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTask(Task task);

    Optional<Comment> findByIdAndTask(long id, Task task);

}
