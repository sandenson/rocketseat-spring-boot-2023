package com.sandenson.todolist.tasks;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findByUserId(UUID userId);
    Optional<TaskModel> findByIdAndUserId(UUID id, UUID userId);
}
