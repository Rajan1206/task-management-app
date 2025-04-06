package com.example.taskservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto {

    private Integer id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String status;

    @NotNull
    private LocalDateTime dueDate;

    private Integer userId;
}
