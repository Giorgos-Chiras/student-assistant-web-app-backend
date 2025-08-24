package com.studenthelper.task.services;

import com.studenthelper.task.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    Task getTaskById(Long id);
    List<Task> getAllTasks();

    void addTask(Task task);

    void updateTask(Long id,Task updatedTask);
    void deleteTaskById(Long id);
}
