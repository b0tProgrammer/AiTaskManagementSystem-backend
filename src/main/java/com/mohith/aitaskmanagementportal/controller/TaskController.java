package com.mohith.aitaskmanagementportal.controller;

import com.mohith.aitaskmanagementportal.dto.ApiResponse;
import com.mohith.aitaskmanagementportal.model.Task;
import com.mohith.aitaskmanagementportal.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Task>> createTask(@RequestBody Task task) {
        Task savedTask = taskService.createTask(task, getCurrentUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Task created successfully", savedTask));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Task>>> getAllMyTasks() {
        List<Task> tasks = taskService.getAllTasksForUser(getCurrentUsername());
        return ResponseEntity.ok(new ApiResponse<>(true, "Tasks fetched successfully", tasks));
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Task>> getTask(@RequestParam Long id) {
        Task task = taskService.getTaskById(id, getCurrentUsername());
        return ResponseEntity.ok(new ApiResponse<>(true, "Task fetched successfully", task));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Task>> updateTask(@RequestBody Task task) {
        System.out.println(task);
        Task updatedTask = taskService.updateTask(task, getCurrentUsername());
        return ResponseEntity.ok(new ApiResponse<>(true, "Task updated successfully", updatedTask));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@RequestParam Long id) {
        taskService.deleteTask(id, getCurrentUsername());
        return ResponseEntity.ok(new ApiResponse<>(true, "Task deleted successfully", null));
    }

    @GetMapping("/aiDesc")
    public ResponseEntity<ApiResponse<Task>> getAiDesc(@RequestParam String title) {
        Task aiDesc = taskService.getAiDesc(title, getCurrentUsername());
        return ResponseEntity.ok(new ApiResponse<>(true, "AI Description generated successfully", aiDesc));
    }
}