package com.mohith.aitaskmanagementportal.dto;

import com.mohith.aitaskmanagementportal.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiTaskExtraction {
    private String description;
    private Task.Priority priority;
    private String estimatedTime;
}
