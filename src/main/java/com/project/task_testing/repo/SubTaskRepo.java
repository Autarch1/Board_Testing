package com.project.task_testing.repo;

import com.project.task_testing.model.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubTaskRepo extends JpaRepository<SubTask, Long> {

}
