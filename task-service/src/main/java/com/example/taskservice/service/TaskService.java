package com.example.taskservice.service;

import com.example.taskservice.dto.TaskDto;

import java.util.List;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto, String emailId);
    TaskDto updateTask(Integer id, TaskDto taskDto, String emailId);
    void deleteTask(Integer id, Integer userId);
    TaskDto getTaskById(Integer id);
    List<TaskDto> getAllTasks(Integer id);
    TaskDto markTaskAsComplete(Integer id, String userEmail);
}
