package com.studenthelper.course;

import com.studenthelper.course.DTO.CourseCreateDTO;
import com.studenthelper.course.DTO.CourseDTO;
import com.studenthelper.course.DTO.CourseUpdateDTO;
import com.studenthelper.professor.DTO.ProfessorDTO;
import com.studenthelper.task.DTO.TaskDTO;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeEach
    void contextLoads() {

    }

    @Test
    void getCourseByIdTest() {
        ResponseEntity<CourseDTO> response = restTemplate.withBasicAuth("alice123", "password1")
                .getForEntity("/courses/1", CourseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        CourseDTO course = response.getBody();

        assertThat(course).isNotNull();
        assertThat(course.getId()).isEqualTo(1);
        assertThat(course.getCourseId()).isEqualTo("CS101");
        assertThat(course.getName()).isEqualTo("Intro to Programming");
        assertThat(course.getEcts()).isEqualTo(6);
        assertThat(course.getProfessorName()).isEqualTo("John Smith");
    }

    @Test
    void getCourseOfOtherUserByIdTest() {
        ResponseEntity<CourseDTO> response = restTemplate.withBasicAuth("alice123", "password1")
                .getForEntity("/courses/2", CourseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


    @Test
    void getCourseByIdThatDoesNotExistTest() {
        ResponseEntity<CourseDTO> response = restTemplate.withBasicAuth("alice123", "password1").
                getForEntity("/courses/9999", CourseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getAllCoursesTest() {
        ResponseEntity<CourseDTO[]> response = restTemplate.withBasicAuth("alice123", "password1")
                .getForEntity("/courses", CourseDTO[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    void addCourseTest() {
        ResponseEntity<ProfessorDTO> professorResponse =
                restTemplate.withBasicAuth("alice123", "password1")
                        .getForEntity("/professors/1", ProfessorDTO.class);

        assertThat(professorResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProfessorDTO professorDTO = professorResponse.getBody();
        assertThat(professorDTO).isNotNull();
        assertThat(professorDTO.getId()).isEqualTo(1L);
        assertThat(professorDTO.getName()).isEqualTo("John Smith");

        CourseCreateDTO newCourse = new CourseCreateDTO();
        newCourse.setCourseId("CS202");
        newCourse.setName("Algorithms");
        newCourse.setEcts(6);

        newCourse.setProfessorId(professorDTO.getId());

        ResponseEntity<Void> postResponse =
                restTemplate.withBasicAuth("alice123", "password1").
                        postForEntity("/courses", newCourse, Void.class);

        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI location = postResponse.getHeaders().getLocation();
        assertThat(location).isNotNull();

        ResponseEntity<CourseDTO> getResponse =
                restTemplate.withBasicAuth("alice123", "password1").
                        getForEntity(location, CourseDTO.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        CourseDTO course = getResponse.getBody();
        assertThat(course).isNotNull();

        assertThat(course.getCourseId()).isEqualTo("CS202");
        assertThat(course.getName()).isEqualTo("Algorithms");
        assertThat(course.getEcts()).isEqualTo(6);
        assertThat(course.getProfessorName()).isEqualTo("John Smith");
    }

    @Test
    @DirtiesContext
    void updateCourseTest() {
        CourseUpdateDTO updatedCourse = new CourseUpdateDTO("CS202", "Intro to Programming",
                6, 2L);

        HttpEntity<CourseUpdateDTO> request = new HttpEntity<>(updatedCourse);

        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").
                exchange("/courses/1", HttpMethod.PUT, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<CourseDTO> getResponse = restTemplate.withBasicAuth("alice123", "password1").
                getForEntity("/courses/1", CourseDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        CourseDTO courseDTO = getResponse.getBody();
        assertThat(courseDTO).isNotNull();
        assertThat(courseDTO.getId()).isEqualTo(1);
        assertThat(courseDTO.getCourseId()).isEqualTo("CS202");
        assertThat(courseDTO.getName()).isEqualTo("Intro to Programming");
        assertThat(courseDTO.getEcts()).isEqualTo(6);
        assertThat(courseDTO.getProfessorName()).isEqualTo("Lisa Williams");
    }

    @Test
    void updateCourseThatDoesNotExistTest() {
        CourseUpdateDTO updatedCourse = new CourseUpdateDTO("CS202", "Intro to Programming",
                6, 2L);

        HttpEntity<CourseUpdateDTO> request = new HttpEntity<>(updatedCourse);

        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").
                exchange("/courses/9999", HttpMethod.PUT, request, Void.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    void updateCourseOfOtherUserTest() {
        CourseUpdateDTO updatedCourse = new CourseUpdateDTO("CS202", "Intro to Programming",
                6, 2L);

        HttpEntity<CourseUpdateDTO> request = new HttpEntity<>(updatedCourse);

        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").
                exchange("/courses/2", HttpMethod.PUT, request, Void.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    @DirtiesContext
    void deleteCourseTest() {

        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").
                exchange("/courses/1",
                        HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<CourseDTO> getResponse = restTemplate.
                withBasicAuth("alice123", "password1").
                getForEntity("/courses/1", CourseDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        ResponseEntity<TaskDTO> getTaskResponse = restTemplate.
                withBasicAuth("alice123", "password1")
                .getForEntity("/tasks/1", TaskDTO.class);
        assertThat(getTaskResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


    @Test
    void deleteCourseOfOtherUserTest() {

        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").
                exchange("/courses/2",
                        HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    void deleteCourseThatDoesNotExistTest() {
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").
                exchange("/courses/999",
                        HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}
