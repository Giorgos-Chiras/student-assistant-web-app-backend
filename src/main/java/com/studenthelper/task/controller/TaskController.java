package com.studenthelper.task.controller;

import com.studenthelper.exceptions.AccessDeniedException;
import com.studenthelper.task.DTO.TaskCreateDTO;
import com.studenthelper.task.DTO.TaskDTO;
import com.studenthelper.task.DTO.TaskUpdateDTO;
import com.studenthelper.task.exception.TaskNotFoundException;
import com.studenthelper.task.mapper.TaskMapper;
import com.studenthelper.task.model.Task;
import com.studenthelper.task.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskMapper taskMapper;

    private  final TaskService taskService;

    TaskController(TaskService taskService,TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        TaskDTO  taskDTO = taskMapper.toDTO(task);
        return ResponseEntity.ok(taskDTO);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks() {
        List<Task> tasks = taskService.getAllTasks();
        List<TaskDTO> taskDTOS = new ArrayList<>();
        for (Task task : tasks) {
            TaskDTO taskDTO = taskMapper.toDTO(task);
            taskDTOS.add(taskDTO);
        }
        return ResponseEntity.ok(taskDTOS);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskCreateDTO taskCreateDTO) {
        Task task = taskMapper.toEntity(taskCreateDTO);
        taskService.addTask(task);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable Long id, @RequestBody TaskUpdateDTO taskUpdateDTO) {
        Task task = taskMapper.toEntity(taskUpdateDTO);
        taskService.updateTask(id,task);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
    @ControllerAdvice
    public static class TaskExceptionHandler {
        @ExceptionHandler(TaskNotFoundException.class)
        public ResponseEntity<String> handleTaskNotFound(TaskNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }
}
