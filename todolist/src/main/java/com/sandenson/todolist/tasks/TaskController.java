package com.sandenson.todolist.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tasks")
public class TaskController {
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping
    public ResponseEntity create(@RequestBody TaskModel model) {
        System.out.println("Controller has been reached");
        var createdTask = this.taskRepository.save(model);
        return ResponseEntity.ok(createdTask);
    }
}
