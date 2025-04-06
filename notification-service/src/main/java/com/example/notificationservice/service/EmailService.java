package com.example.notificationservice.service;

import com.example.notificationservice.model.TaskEvent;

public interface EmailService {
    void sendTaskNotification(TaskEvent event);
}
