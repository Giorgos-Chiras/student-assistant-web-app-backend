package com.studenthelper.task;

import com.studenthelper.course.DTO.CourseDTO;
import com.studenthelper.task.DTO.TaskCreateDTO;
import com.studenthelper.task.DTO.TaskDTO;
import com.studenthelper.task.DTO.TaskUpdateDTO;
import com.studenthelper.user.DTO.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeEach
    public void contextLoads() {

    }

    @Test
    public void getTaskByIdTest() {
        ResponseEntity<TaskDTO> response = restTemplate.withBasicAuth("alice123", "password1")
                .getForEntity("/tasks/1", TaskDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        TaskDTO task = response.getBody();
        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo(1);
        assertThat(task.getName()).isEqualTo("Project Proposal");
        assertThat(task.getDescription()).isEqualTo("Submit project proposal document");
        assertThat(task.getDueDate()).isEqualTo(LocalDate.of(2025, 9, 15));
        assertThat(task.getDueTime()).isEqualTo(LocalTime.of(17, 0));
        assertThat(task.getCourseName()).isEqualTo("Intro to Programming");
        assertThat(task.getUserName()).isEqualTo("alice123");
        assertThat(task.getGrade()).isEqualTo("75/100");
        assertThat(task.isCompleted()).isEqualTo(true);

    }

    @Test
    public void getTaskOfOtherUserByIdTest() {
        ResponseEntity<TaskDTO> response = restTemplate.withBasicAuth("alice123", "password1")
                .getForEntity("/tasks/3", TaskDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void getTaskThatDoesNotExistTest() {
        ResponseEntity<TaskDTO> response = restTemplate.withBasicAuth("alice123", "password1")
                .getForEntity("/tasks/9999", TaskDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void getAllTasksTest() {
        ResponseEntity<List> response = restTemplate.withBasicAuth("alice123", "password1")
                .getForEntity("/tasks", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    public void createTaskTest() {
        LocalDate dueDate = LocalDate.of(2025, 9, 15);
        LocalTime dueTime = LocalTime.of(17, 0);
        TaskCreateDTO taskCreateDTO = new TaskCreateDTO("Test Task", "Test description",
                dueDate,
                dueTime,
                3L);

        ResponseEntity<Void> response = restTemplate.withBasicAuth("bob456", "password2")
                .postForEntity("/tasks", taskCreateDTO, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI location = response.getHeaders().getLocation();
        assertThat(location).isNotNull();

        ResponseEntity<TaskDTO> getResponse = restTemplate.withBasicAuth("bob456", "password2")
                .getForEntity(location, TaskDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        TaskDTO task = getResponse.getBody();
        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo(4);
        assertThat(task.getName()).isEqualTo("Test Task");
        assertThat(task.getDescription()).isEqualTo("Test description");
        assertThat(task.getDueDate()).isEqualTo(dueDate);
        assertThat(task.getDueTime()).isEqualTo(dueTime);
        assertThat(task.getUserName()).isEqualTo("bob456");
        assertThat(task.getCourseName()).isEqualTo("Quantum Mechanics");
        assertThat(task.getGrade()).isEqualTo("");
        assertThat(task.isCompleted()).isEqualTo(false);
    }

    @Test
    @DirtiesContext
    public void updateTaskTest() {
        LocalDate dueDate = LocalDate.of(2025, 9, 20);
        LocalTime dueTime = LocalTime.of(18, 0);

        TaskUpdateDTO updatedTask = new TaskUpdateDTO("Project Proposal 1",
                "Submit project proposal document",
                dueDate, dueTime, 1L,"90/100",true);

        HttpEntity<TaskUpdateDTO> request = new HttpEntity<>(updatedTask);

        ResponseEntity<Void> response = restTemplate.withBasicAuth("bob456", "password2")
                .exchange("/tasks/2", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<TaskDTO> getResponse = restTemplate.withBasicAuth("bob456","password2")
                .getForEntity("/tasks/2", TaskDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        TaskDTO task = getResponse.getBody();
        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo(2);
        assertThat(task.getName()).isEqualTo("Project Proposal 1");
        assertThat(task.getDescription()).isEqualTo("Submit project proposal document");
        assertThat(task.getDueDate()).isEqualTo(dueDate);
        assertThat(task.getDueTime()).isEqualTo(dueTime);
        assertThat(task.getUserName()).isEqualTo("bob456");
        assertThat(task.getCourseName()).isEqualTo("Intro to Programming");
        assertThat(task.getGrade()).isEqualTo("90/100");
        assertThat(task.isCompleted()).isEqualTo(true);
    }

    @Test
    public void updateTaskOfOtherUserTest() {
        LocalDate dueDate = LocalDate.of(2025, 9, 20);
        LocalTime dueTime = LocalTime.of(18, 0);

        TaskUpdateDTO updatedTask = new TaskUpdateDTO("Project Proposal 1",
                "Submit project proposal document",
                dueDate, dueTime, 1L,"90/100",true);

        HttpEntity<TaskUpdateDTO> request = new HttpEntity<>(updatedTask);

        ResponseEntity<Void> response = restTemplate.withBasicAuth("bob456", "password2")
                .exchange("/tasks/1", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


        @Test
    public void updateTaskThatDoesNotExistTest() {
        LocalDate dueDate = LocalDate.of(2025, 9, 20);
        LocalTime dueTime = LocalTime.of(18, 0);

        TaskUpdateDTO updatedTask = new TaskUpdateDTO("Project Proposal 1",
                "Submit project proposal document",
                dueDate, dueTime, 1L,"90/100",true);

        HttpEntity<TaskUpdateDTO> request = new HttpEntity<>(updatedTask);
        ResponseEntity<TaskDTO> response = restTemplate.withBasicAuth("alice123","password1")
                .exchange("/tasks/9999", HttpMethod.PUT, request, TaskDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void deleteTaskTest() {
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123","password1")
                .exchange("/tasks/1", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<TaskDTO> getResponse = restTemplate.withBasicAuth("alice123","password1")
                .getForEntity("/tasks/1", TaskDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        ResponseEntity<UserDTO> userResponse = restTemplate.withBasicAuth("alice123","password1")
                .getForEntity("/user/1", UserDTO.class);
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<CourseDTO> courseResponse = restTemplate.withBasicAuth("alice123","password1")
                .getForEntity("/user/1", CourseDTO.class);
        assertThat(courseResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void deleteTaskOfOtherUserTest() {
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123","password1").
                exchange("/tasks/2", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void deleteTaskThatDoesNotExistTest() {
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123","password1").
                exchange("/tasks/9999", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
