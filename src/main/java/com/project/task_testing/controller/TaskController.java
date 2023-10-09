package com.project.task_testing.controller;

import com.project.task_testing.model.SubTask;
import com.project.task_testing.model.Task;
import com.project.task_testing.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks") // Add a base path for the controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping("/addMainTask")
    public ResponseEntity<Task> addMainTask(@RequestBody Task mainTask) {
        Task savedMainTask = taskService.saveTask(mainTask);
        return ResponseEntity.ok(savedMainTask);
    }

    @PostMapping("/addSubtask")
    public ResponseEntity<Object> addSubtask(@RequestBody SubTask subtask, @RequestParam("mainTaskId") Long mainTaskId) {
        try {
            // Find the main task by its ID
            Optional<Task> mainTaskOptional = taskService.getTaskById(mainTaskId);

            if (mainTaskOptional.isPresent()) {
                Task mainTask = mainTaskOptional.get();

                // Set the main task for the subtask
                subtask.setMainTask(mainTask);

                // Save the subtask, which will also update the association in the database
                SubTask savedSubtask = taskService.saveSubtask(subtask);

                return ResponseEntity.ok(savedSubtask);
            } else {
                // Return a JSON response with an error message
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Main task not found.");
            }
        } catch (Exception e) {
            // Return a JSON response with an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding subtask: " + e.getMessage());
        }
    }

    @PostMapping("/moveSubtask")
    public ResponseEntity<String> moveSubtask(@RequestBody Map<String, Long> subtaskData) {
        try {
            Long subtaskId = subtaskData.get("subtaskId");
            Long newMainTaskId = subtaskData.get("newMainTaskId");

            // Fetch the subtask and new main task from the service
            Optional<SubTask> subtaskOptional = taskService.getSubtaskById(subtaskId);
            Optional<Task> newMainTaskOptional = taskService.getTaskById(newMainTaskId);

            if (subtaskOptional.isPresent() && newMainTaskOptional.isPresent()) {
                SubTask subtask = subtaskOptional.get();
                Task newMainTask = newMainTaskOptional.get();

                // Remove the subtask from its current main task's subtasks list
                subtask.getMainTask().getSubtasks().remove(subtask);

                // Set the new main task for the subtask
                subtask.setMainTask(newMainTask);

                // Add the subtask to the new main task's subtasks list
                newMainTask.getSubtasks().add(subtask);

                // Save both the subtask and the new main task
                taskService.saveSubtask(subtask);
                taskService.saveTask(newMainTask);

                return ResponseEntity.ok("Subtask moved successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subtask or new main task not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error moving subtask: " + e.getMessage());
        }
    }

    // Other methods for updating task order and subtask order can be similarly modified.
}
