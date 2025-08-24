package com.studenthelper.task.repository;

import com.studenthelper.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsTaskById(Long id);

    Optional<Task> findByIdAndUserId(Long id, Long userId);

    Optional<List<Task>> findAllByUserId(Long userId);
}
