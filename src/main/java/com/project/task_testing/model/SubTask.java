package com.project.task_testing.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity

public class SubTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Define the relationship for the main task
    @ManyToOne
    @JoinColumn(name = "main_task_id")
    private Task mainTask;

    public SubTask(Long id, String name, Task mainTask) {
        this.id = id;
        this.name = name;
        this.mainTask = mainTask;
    }

    public SubTask() {

    }

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

    public Task getMainTask() {
        return mainTask;
    }

    public void setMainTask(Task mainTask) {
        this.mainTask = mainTask;
    }
}
