package com.studenthelper.task.services;

import com.studenthelper.exceptions.AccessDeniedException;
import com.studenthelper.service.SecurityService;
import com.studenthelper.task.exception.TaskNotFoundException;
import com.studenthelper.task.model.Task;
import com.studenthelper.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {


    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SecurityService securityService;

    @Override
    public Task getTaskById(Long id) {
       return taskRepository.findByIdAndUserId(id,securityService.getCurrentUserId()).orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAllByUserId(securityService.getCurrentUserId()).get();
    }

    @Override
    public void addTask(Task task) {
        if(task.getUser().getId() != securityService.getCurrentUserId()){
            throw new AccessDeniedException("You do not have permission to add task");
        }
        taskRepository.save(task);
    }

    @Override
    public void updateTask(Long id,Task updatedTask) {
        Task task= getTaskById(id);
        task.setDescription(updatedTask.getDescription());
        task.setName(updatedTask.getName());
        task.setDueDate(updatedTask.getDueDate());
        task.setDueTime(updatedTask.getDueTime());
        task.setUser(updatedTask.getUser());
        task.setCourse(updatedTask.getCourse());
        task.setGrade(updatedTask.getGrade());
        task.setCompleted(updatedTask.getCompleted());
        taskRepository.save(task);
    }

    @Override
    public void deleteTaskById(Long id) {
        Task task= getTaskById(id);
        taskRepository.delete(task);
    }


}
