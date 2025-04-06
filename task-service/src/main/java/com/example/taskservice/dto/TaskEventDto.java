package com.example.taskservice.dto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TaskEventDto {
    private Integer taskId;
    private String title;
    private String action; // CREATED, UPDATED, COMPLETED
    private String emailId;

}
