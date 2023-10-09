package com.project.task_testing.service;

import com.project.task_testing.model.SubTask;
import com.project.task_testing.model.Task;
import com.project.task_testing.repo.SubTaskRepo;
import com.project.task_testing.repo.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepo taskRepo;
    private final SubTaskRepo subTaskRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo, SubTaskRepo subTaskRepo) {
        this.taskRepo = taskRepo;
        this.subTaskRepo = subTaskRepo;
    }

    public List<Task> getAllTasks(){
        return taskRepo.findAll();
    }

    public List<SubTask> getAllSubTask(){
        return subTaskRepo.findAll();
    }
    public Task saveTask(Task task) {
        return taskRepo.save(task);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepo.findById(id);
    }

    public void deleteTask(Long id) {
        taskRepo.deleteById(id);
    }

    public SubTask saveSubtask(SubTask subtask) {
        return subTaskRepo.save(subtask);
    }

    public Optional<SubTask> getSubtaskById(Long id) {
        return subTaskRepo.findById(id);
    }


}
