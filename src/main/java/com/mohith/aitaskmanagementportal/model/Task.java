package com.mohith.aitaskmanagementportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Enumerated(EnumType.STRING)
    private Status status = Status.TODO;
    private LocalDate dueDate;
    private String estimatedTime;
    private LocalDateTime createdTimestamp = LocalDateTime.now();
    public enum Priority { LOW, MEDIUM, HIGH }
    public enum Status { TODO, IN_PROGRESS, DONE }
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "username", nullable = false)
    private Users user;
}