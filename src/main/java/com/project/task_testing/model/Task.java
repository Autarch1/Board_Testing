package com.project.task_testing.model;


import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Define the relationship for subtasks
    @OneToMany(mappedBy = "mainTask", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<SubTask> subtasks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubTask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<SubTask> subtasks) {
        this.subtasks = subtasks;
    }

    public Task(Long id, String name, List<SubTask> subtasks) {
        this.id = id;
        this.name = name;
        this.subtasks = subtasks;
    }
    public Task(){

    }
}

