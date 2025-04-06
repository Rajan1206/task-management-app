package com.example.taskservice.mapper;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.dto.TaskEventDto;
import com.example.taskservice.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {


    public Task mapToEntity(TaskDto dto) {
        return Task.builder()
                   .id(dto.getId())
                   .title(dto.getTitle())
                   .description(dto.getDescription())
                   .dueDate(dto.getDueDate())
                   .status(dto.getStatus())
                   .userId(dto.getUserId())
                   .build();
    }

    public TaskDto mapToDto(Task task) {
        return TaskDto.builder()
                      .id(task.getId())
                      .title(task.getTitle())
                      .description(task.getDescription())
                      .dueDate(task.getDueDate())
                      .status(task.getStatus())
                      .userId(task.getUserId())
                      .build();
    }

    public TaskEventDto mapToEventDto(Task task, String action, String emailId) {
        return TaskEventDto.builder()
                           .taskId(task.getId())
                           .title(task.getTitle())
                           .emailId(emailId)
                           .action(action)
                           .build();

    }
}
