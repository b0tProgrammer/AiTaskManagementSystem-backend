package com.mohith.aitaskmanagementportal.service;

import com.mohith.aitaskmanagementportal.dto.AiTaskExtraction;
import com.mohith.aitaskmanagementportal.exception.ResourceNotFoundException;
import com.mohith.aitaskmanagementportal.model.Task;
import com.mohith.aitaskmanagementportal.model.Users;
import com.mohith.aitaskmanagementportal.dao.TaskRepo;
import com.mohith.aitaskmanagementportal.dao.UsersRepo;
import io.jsonwebtoken.JwtParserBuilder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepo taskRepo;
    private final UsersRepo usersRepo;

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    public TaskService(TaskRepo taskRepo, UsersRepo usersRepo) {
        this.taskRepo = taskRepo;
        this.usersRepo = usersRepo;
    }

    public Task createTask(Task task, String username) {
        Users user = usersRepo.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        task.setUser(user);
        return taskRepo.save(task);
    }

    public List<Task> getAllTasksForUser(String username) {
        return taskRepo.findByUser_Username(username);
    }

    public Task getTaskById(Long id, String username) {
        Task task = taskRepo.findById(Math.toIntExact(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (!task.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        return task;
    }

    public Task updateTask(Task updatedTask, String username) {
        if (updatedTask.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task ID is required");
        }

        Task existingTask = getTaskById(updatedTask.getId(), username);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setEstimatedTime(updatedTask.getEstimatedTime());

        return taskRepo.save(existingTask);
    }

    public void deleteTask(Long id, String username) {
        Task task = getTaskById(id, username);
        taskRepo.delete(task);
    }

    public Task getAiDesc(String title,String userName) {
        Users user = usersRepo.findById(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userName));
        String prompt = "You are a professional task management assistant. " +
                "Analyze this task title: '" + title + "'. " +
                "Generate a 1-2 sentence description, and assign a priority (LOW, MEDIUM, or HIGH). " +
                "Provide an 'estimatedTime' as a short string duration (e.g., '30 minutes', '2 hours', '1 day').";

        ChatClient chatClient = chatClientBuilder.build();
        AiTaskExtraction extractedData = chatClient.prompt()
                .user(prompt)
                .call()
                .entity(AiTaskExtraction.class);
        Task newTask = new Task();
        newTask.setTitle(title);
        assert extractedData != null;
        newTask.setDescription(extractedData.getDescription());
        newTask.setPriority(extractedData.getPriority());
        newTask.setEstimatedTime(extractedData.getEstimatedTime());
        newTask.setStatus(Task.Status.TODO);
        newTask.setUser(user);
        return newTask;
    }
}