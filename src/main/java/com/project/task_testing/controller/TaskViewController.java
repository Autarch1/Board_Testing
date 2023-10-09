package com.project.task_testing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaskViewController {
    @GetMapping(value = "/")
    public String view(){
        return "Task";
    }
}
