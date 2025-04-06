package com.example.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEvent {
    private Integer taskId;
    private String title;
    private String action; // CREATED, UPDATED, COMPLETED
    private String emailId;
}
