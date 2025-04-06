package com.example.taskservice.repository;

import com.example.taskservice.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    Optional<List<Task>> findAllByUserId(Integer userId);
    Optional<Task> findByIdAndUserId(Integer id, Integer userId);
}
