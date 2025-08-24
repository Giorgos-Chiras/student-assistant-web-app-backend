package com.studenthelper.user;

import com.studenthelper.course.DTO.CourseCreateDTO;
import com.studenthelper.course.DTO.CourseDTO;
import com.studenthelper.course.DTO.CourseUserDTO;
import com.studenthelper.task.DTO.TaskCreateDTO;
import com.studenthelper.task.DTO.TaskUserDTO;
import com.studenthelper.user.DTO.*;
import com.studenthelper.user.model.User;
import com.studenthelper.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;


    String BASE_URL = "/user";
    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void contextLoads() {
    }


    @Test
    void getUserByIdTest() {
        ResponseEntity<UserDTO> response =
                restTemplate.withBasicAuth("alice123", "password1").getForEntity("/user/{id}", UserDTO.class, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        //Validate User
        UserDTO user = response.getBody();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo("alice123");
        assertThat(user.getEmail()).isEqualTo("alice@example.com");
        assertThat(user.getAge()).isEqualTo(20);
        assertThat(user.getUniversity()).isEqualTo("Harvard University");
        assertThat(user.getMajor()).isEqualTo("Computer Science");

        //Validate Courses
        List<CourseUserDTO> courses = user.getCourses();
        assertThat(courses).isNotNull();
        assertThat(courses.size()).isEqualTo(1);
        assertThat(courses.get(0)).isNotNull();
        assertThat(courses.get(0).getId()).isEqualTo(1);
        assertThat(courses.get(0).getName()).isEqualTo("Intro to Programming");

        //Validate Tasks
        List<TaskUserDTO> tasks = user.getTasks();
        TaskUserDTO task = tasks.get(0);
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(1);
        assertThat(task).isNotNull();
        assertThat(task.getTaskId()).isEqualTo(1);
        assertThat(task.getTaskName()).isEqualTo("Project Proposal");
        assertThat(task.getTaskCourse()).isEqualTo("Intro to Programming");

    }

    @Test
    void getUserThatDoesNotExistTestById() {
        ResponseEntity<UserDTO> response = restTemplate.withBasicAuth("alice123", "password1").getForEntity("/user/9999", UserDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getUserOfOtherUerByIdTest() {
        ResponseEntity<UserDTO> response = restTemplate.withBasicAuth("alice123", "password1").getForEntity("/user/2", UserDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getUserByNameTest() {
        ResponseEntity<UserDTO> response = restTemplate.withBasicAuth("alice123", "password1").getForEntity("/user/username/alice123", UserDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        //Validate User
        UserDTO user = response.getBody();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo("alice123");
        assertThat(user.getEmail()).isEqualTo("alice@example.com");
        assertThat(user.getAge()).isEqualTo(20);
        assertThat(user.getUniversity()).isEqualTo("Harvard University");
        assertThat(user.getMajor()).isEqualTo("Computer Science");

        //Validate Courses
        List<CourseUserDTO> courses = user.getCourses();
        assertThat(courses).isNotNull();
        assertThat(courses.size()).isEqualTo(1);
        assertThat(courses.get(0)).isNotNull();
        assertThat(courses.get(0).getId()).isEqualTo(1);
        assertThat(courses.get(0).getName()).isEqualTo("Intro to Programming");

        //Validate Tasks
        List<TaskUserDTO> tasks = user.getTasks();
        TaskUserDTO task = tasks.get(0);
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(1);
        assertThat(task).isNotNull();
        assertThat(task.getTaskId()).isEqualTo(1);
        assertThat(task.getTaskName()).isEqualTo("Project Proposal");
        assertThat(task.getTaskCourse()).isEqualTo("Intro to Programming");
    }

    @Test
    void getUserThatDoesNotExistByNameTest() {
        ResponseEntity<UserDTO> response = restTemplate.withBasicAuth("alice123", "password1").getForEntity("/user/username/fakeuser", UserDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getUserOfOtherUserByNameTest() {
        ResponseEntity<UserDTO> response = restTemplate.withBasicAuth("alice123", "password1").
                getForEntity("/user/2", UserDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DirtiesContext
    void createUserTest() {
        UserCreateDTO user = new UserCreateDTO("testuser", "Securepass123!", "Securepass123!", 21,
                "testuser@example.com",
                "Test University", "Computer Science");

        ResponseEntity<Void> response = restTemplate.postForEntity("/user", user, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI uri = response.getHeaders().getLocation();

        ResponseEntity<UserDTO> getResponse = restTemplate.withBasicAuth("testuser", "Securepass123!").getForEntity(uri, UserDTO.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserDTO savedUser = getResponse.getBody();
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(6L);
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("testuser@example.com");
        assertThat(savedUser.getUniversity()).isEqualTo("Test University");
        assertThat(savedUser.getMajor()).isEqualTo("Computer Science");
        assertThat(savedUser.getCourses()).isEmpty();
        assertThat(savedUser.getTasks()).isEmpty();

    }

    @Test
    void createUserWhileAuthenticatedTest() {

        UserCreateDTO user = new UserCreateDTO("testuser", "Securepass123!", "Securepass123!", 21,
                "testuser@example.com",
                "Test University", "Computer Science");

        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").postForEntity("/user", user, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    void createUserThatAlreadyExistsTest() {
        UserCreateDTO newUser = new UserCreateDTO("alice123", "Password1!", "Password1!",
                20, "alice@example.com",
                "Harvard University", "Computer Science");

        ResponseEntity<Void> response = restTemplate.
                postForEntity("/user", newUser, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

    }

    @Test
    void createUserThatAlreadyExistsWhileAuthenticatedTest() {
        UserCreateDTO newUser = new UserCreateDTO("alice123", "Password1!", "Password1!",
                20, "alice@example.com",
                "Harvard University", "Computer Science");

        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").postForEntity("/user", newUser, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }


    @Test
    @DirtiesContext
    void addCourseToUserTest() {
        CourseCreateDTO newCourse = new CourseCreateDTO("TC01", "Test Course", 6, 1L);
        HttpEntity<CourseCreateDTO> request = new HttpEntity<>(newCourse);

        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").exchange("/courses", HttpMethod.POST, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<UserDTO> getResponse = restTemplate.withBasicAuth("alice123", "password1").getForEntity("/user/1", UserDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserDTO savedUser = getResponse.getBody();
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getUsername()).isEqualTo("alice123");

        List<CourseUserDTO> courses = savedUser.getCourses();
        int taskCount = courses.size();
        CourseUserDTO courseUserDTO = courses.get(taskCount - 1);


        assertThat(courseUserDTO).isNotNull();
        assertThat(courseUserDTO.getCourseId()).isEqualTo("TC01");
        assertThat(courseUserDTO.getName()).isEqualTo("Test Course");

        ResponseEntity<CourseDTO> courseGetResponse = restTemplate.withBasicAuth("alice123", "password1").getForEntity(
                "/courses/{id}", CourseDTO.class, 6);

        assertThat(courseGetResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        CourseDTO course = courseGetResponse.getBody();
        assertThat(course).isNotNull();

        assertThat(course.getId()).isNotNull();
        assertThat(course.getName()).isEqualTo("Test Course");
        assertThat(course.getCourseId()).isEqualTo("TC01");
        assertThat(course.getProfessorName()).isEqualTo("John Smith");

    }

    @Test
    void addCourseToUserWithProfessorFromDifferentUserTest() {
        CourseCreateDTO newCourse = new CourseCreateDTO("TC01", "Test Course", 6, 2L);
        HttpEntity<CourseCreateDTO> request = new HttpEntity<>(newCourse);

        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").
                exchange("/courses", HttpMethod.POST, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DirtiesContext
    public void addTaskToUserTest() {
        LocalDate date = LocalDate.of(2025, 10, 20);
        LocalTime time = LocalTime.of(8, 0, 0);

        TaskCreateDTO newTask = new TaskCreateDTO("T01", "Test Task", date, time, 1L);

        HttpEntity<TaskCreateDTO> request = new HttpEntity<>(newTask);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123","password1").
                exchange("/tasks",
                HttpMethod.POST, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<UserDTO> userGetResponse = restTemplate.withBasicAuth("alice123","password1").getForEntity("/user/1", UserDTO.class);
        assertThat(userGetResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserDTO savedUser = userGetResponse.getBody();
        List<TaskUserDTO> tasks = savedUser.getTasks();

        int taskCount = tasks.size();
        TaskUserDTO task = tasks.get(taskCount - 1);

        assertThat(task).isNotNull();
        assertThat(task.getTaskId()).isEqualTo(4);
        assertThat(task.getTaskName()).isEqualTo("T01");
        assertThat(task.getTaskCourse()).isEqualTo("Intro to Programming");

    }

    @Test
    @DirtiesContext
    void updateUserTest() {
        UserUpdateDTO updatedUser = new UserUpdateDTO("alice", 20, "alice@example.com", "Business School", "Economics");
        HttpEntity<UserUpdateDTO> request = new HttpEntity<>(updatedUser);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").exchange(BASE_URL + "/1", HttpMethod.PUT, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<UserDTO> getResponse = restTemplate.withBasicAuth("alice", "password1").getForEntity(BASE_URL + "/1", UserDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserDTO user = getResponse.getBody();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo("alice");
        assertThat(user.getEmail()).isEqualTo("alice@example.com");
        assertThat(user.getAge()).isEqualTo(20);
        assertThat(user.getUniversity()).isEqualTo("Business School");
        assertThat(user.getMajor()).isEqualTo("Economics");

    }

    @Test
    void updateOtherUserTest() {

        UserUpdateDTO updatedUser = new UserUpdateDTO("alice", 20, "alice@example.com", "Business School", "Economics");
        HttpEntity<UserUpdateDTO> request = new HttpEntity<>(updatedUser);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").exchange(BASE_URL + "/2", HttpMethod.PUT, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    @DirtiesContext
    void updateUserEmailTest() {
        UserUpdateEmailDTO newEmail = new UserUpdateEmailDTO("newemail@gmail.com");
        HttpEntity<UserUpdateEmailDTO> request = new HttpEntity<>(newEmail);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").exchange("/user/1/email", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<UserDTO> getResponse = restTemplate.withBasicAuth("alice123", "password1").getForEntity(BASE_URL + "/1", UserDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserDTO user = getResponse.getBody();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo("alice123");
        assertThat(user.getEmail()).isEqualTo("newemail@gmail.com");

    }

    @Test
    @DirtiesContext
    void updateOtherUserEmailTest() {
        UserUpdateEmailDTO newEmail = new UserUpdateEmailDTO("newemail@gmail.com");
        HttpEntity<UserUpdateEmailDTO> request = new HttpEntity<>(newEmail);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").exchange("/user/2/email", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void updateUserEmailConflictTest() {
        UserUpdateEmailDTO newEmail = new UserUpdateEmailDTO("alice@example.com");
        HttpEntity<UserUpdateEmailDTO> request = new HttpEntity<>(newEmail);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").exchange("/user/1/email", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

    }

    @Test
    void updateOtherUserEmailConflictTest() {
        UserUpdateEmailDTO newEmail = new UserUpdateEmailDTO("alice@example.com");
        HttpEntity<UserUpdateEmailDTO> request = new HttpEntity<>(newEmail);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").exchange("/user/2/email", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    @DirtiesContext
    void updateUserPasswordTest() {

        UserUpdatePasswordDTO password = new UserUpdatePasswordDTO("password1",
                "NewPass123!", "NewPass123!");

        HttpEntity<UserUpdatePasswordDTO> request = new HttpEntity<>(password);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").
                exchange("/user/1/password", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<UserDTO> getResponse = restTemplate.withBasicAuth("alice123", "NewPass123!").getForEntity(BASE_URL + "/1", UserDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserDTO user = getResponse.getBody();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo("alice123");

        //Get the user and compare password
        User savedUser = userRepository.findById(1L).get();
        assertThat(savedUser).isNotNull();
        assertThat(passwordEncoder.matches("NewPass123!", savedUser.getPassword())).isTrue();

    }

    @Test
    @DirtiesContext
    void updateOtherUserPasswordTest() {

        UserUpdatePasswordDTO password = new UserUpdatePasswordDTO("password1",
                "NewPass123!", "NewPass123!");

        HttpEntity<UserUpdatePasswordDTO> request = new HttpEntity<>(password);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").
                exchange("/user/2/password", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }


    @Test
    public void updateUserPasswordInvalidNewPasswordTest() {
        UserUpdatePasswordDTO password = new UserUpdatePasswordDTO("password1",
                "NenwPass123!", "NewPass123!");

        HttpEntity<UserUpdatePasswordDTO> request = new HttpEntity<>(password);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").exchange("/user/1/password", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void updateOtherUserPasswordInvalidNewPasswordTest() {
        UserUpdatePasswordDTO password = new UserUpdatePasswordDTO("password1",
                "NenwPass123!", "NewPass123!");

        HttpEntity<UserUpdatePasswordDTO> request = new HttpEntity<>(password);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").
                exchange("/user/2/password", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void updateUserPasswordInvalidOldPasswordTest() {
        UserUpdatePasswordDTO password = new UserUpdatePasswordDTO("password1",
                "n", "n");

        HttpEntity<UserUpdatePasswordDTO> request = new HttpEntity<>(password);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").exchange("/user/1/password", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    void updateUserThatDoesNotExistTest() {
        UserUpdateDTO updatedUser = new UserUpdateDTO("alice231", 20, "alice@example.com", "Business School", "Economics");
        HttpEntity<UserUpdateDTO> request = new HttpEntity<>(updatedUser);

        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123","password1").exchange(BASE_URL + "/9999", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DirtiesContext
    void deleteUserTest() {
        //Delete user
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").
                exchange(BASE_URL + "/1", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        //Check if user has been deleted by trying to access user data
        ResponseEntity<User> getResponse = restTemplate.withBasicAuth("alice123", "password1").getForEntity(BASE_URL + "/1", User.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DirtiesContext
    void deleteOtherUserTest() {
        //Delete user
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").
                exchange(BASE_URL + "/2", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    void deleteUserThatDoesNotExistTest() {
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123","password1").
                exchange(BASE_URL + "/999", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}
