package com.sandenson.todolist.tasks;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sandenson.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("tasks")
public class TaskController {
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping
    public ResponseEntity create(@RequestBody TaskModel model, HttpServletRequest request) {
        model.setUserId((UUID) request.getAttribute("userId"));

        var now = LocalDateTime.now();

        if (now.isAfter(model.getStartAt()) || now.isAfter(model.getEndAt())) {
            return ResponseEntity.badRequest().body("A task's start and end dates can't be earlier than today");
        }
        if (model.getStartAt().isAfter(model.getEndAt())) {
            return ResponseEntity.badRequest().body("A task's end date can't be earlier than its start date");
        }

        var createdTask = this.taskRepository.save(model);

        return ResponseEntity.ok(createdTask);
    }

    @GetMapping
    public ResponseEntity list(HttpServletRequest request) {
        var tasks = this.taskRepository.findByUserId((UUID) request.getAttribute("userId"));

        return ResponseEntity.ok(tasks);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable() UUID id, @RequestBody TaskModel model, HttpServletRequest request) {
        // var tasks = this.taskRepository.findByUserId((UUID) request.getAttribute("userId"));
        var taskOpt = this.taskRepository.findById(id);
        
        if (taskOpt.isPresent() && taskOpt.get().getUserId().equals((UUID) request.getAttribute("userId"))) {
            var task = taskOpt.get();

            Utils.copyNonNullProperties(model, task);

            var updated = this.taskRepository.save(task);

            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.status(404).body("Task not found");
        }
    }
}
