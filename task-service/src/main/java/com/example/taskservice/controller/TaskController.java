package com.example.taskservice.controller;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto,
                                              @RequestHeader("X-User-ID") Integer userId,
                                              @RequestHeader("X-User-Email") String emailId) {
        log.info("Authenticated user email: {}", emailId);

        taskDto.setUserId(userId);
        return ResponseEntity.ok(taskService.createTask(taskDto,emailId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Integer id,
                                              @RequestBody TaskDto taskDto,
                                              @RequestHeader("X-User-ID") Integer userId,
                                              @RequestHeader("X-User-Email") String emailId) {
        taskDto.setUserId(userId);
        return ResponseEntity.ok(taskService.updateTask(id, taskDto, emailId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id,
                                             @RequestHeader("X-User-ID") Integer userId) {
        try {
            taskService.deleteTask(id, userId);
            return ResponseEntity.ok().body("Deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks(@RequestHeader("X-User-ID") Integer userId) {
        try {
            return ResponseEntity.ok(taskService.getAllTasks(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Integer id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Object> markComplete(@PathVariable Integer id,
                                                @RequestHeader("X-User-Email") String emailId) {
        try {
            return ResponseEntity.ok(taskService.markTaskAsComplete(id, emailId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
