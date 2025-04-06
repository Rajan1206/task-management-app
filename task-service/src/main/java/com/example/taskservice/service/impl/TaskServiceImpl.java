package com.example.taskservice.service.impl;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.exception.TaskServiceException;
import com.example.taskservice.kafka.TaskEventProducer;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.model.Task;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskEventProducer kafkaProducerService;
    private final TaskMapper mapper;

    @Override
    public TaskDto createTask(TaskDto taskDto, String emailId) {
        Task task = mapper.mapToEntity(taskDto);
        taskRepository.save(task);

        kafkaProducerService.sendTaskEvent(mapper.mapToEventDto(task, "CREATE", emailId));
        return mapper.mapToDto(task);

    }

    @Override
    public TaskDto updateTask(Integer id, TaskDto taskDto, String emailId) {
        Task task = taskRepository.findById(id)
                                  .orElseThrow(() -> new RuntimeException("Task not created."));
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setStatus(taskDto.getStatus());

        taskRepository.save(task);
        kafkaProducerService.sendTaskEvent(mapper.mapToEventDto(task, "UPDATED", emailId));
        return mapper.mapToDto(task);
    }

    @Override
    public void deleteTask(Integer id, Integer userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                                  .orElseThrow(() -> new TaskServiceException("Task not found for this user"));
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDto getTaskById(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskServiceException("Task id is not valid. " +
                                                                                                   "Please verify Task ID."));
        return mapper.mapToDto(task);
    }

    @Override
    public List<TaskDto> getAllTasks(Integer userId) {
        return taskRepository.findAllByUserId(userId)
                             .orElseThrow(() -> new TaskServiceException("No tasks found for this user."))
                             .stream()
                             .map(mapper::mapToDto)
                             .collect(Collectors.toList());
    }

    @Override
    public TaskDto markTaskAsComplete(Integer id, String userEmail) {
        Task task = taskRepository.findById(id)
                                  .orElseThrow(() -> new TaskServiceException("Task id is not valid.Please verify Task ID."));
        task.setStatus("COMPLETED");
        TaskDto taskDto = mapper.mapToDto(taskRepository.save(task));
        kafkaProducerService.sendTaskEvent(mapper.mapToEventDto(task, "COMPLETED", userEmail));
        return taskDto;
    }
}
