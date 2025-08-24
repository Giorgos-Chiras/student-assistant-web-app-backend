package com.studenthelper.professor;

import com.studenthelper.course.DTO.CourseProfessorDTO;
import com.studenthelper.professor.DTO.ProfessorCreateDTO;
import com.studenthelper.professor.DTO.ProfessorDTO;
import com.studenthelper.professor.DTO.ProfessorUpdateDTO;
import com.studenthelper.professor.model.Professor;
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

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfessorControllerTests {

    @Autowired
    TestRestTemplate restTemplate;


    @BeforeEach
    void contextLoads() {
    }

    @Test
    void getProfessorByIdTest() {
        ResponseEntity<ProfessorDTO> response = restTemplate.withBasicAuth("alice123","password1").
                getForEntity("/professors/{id}", ProfessorDTO.class, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ProfessorDTO professor = response.getBody();

        assertThat(professor).isNotNull();
        assertThat(professor.getId()).isEqualTo(1);
        assertThat(professor.getName()).isEqualTo("John Smith");
        assertThat(professor.getEmail()).isEqualTo("john.smith@university.edu");
        assertThat(professor.getOfficeNumber()).isEqualTo("B-101");

        CourseProfessorDTO course = professor.getCourses().get(0);
        assertThat(course).isNotNull();
        assertThat(course.getId()).isEqualTo(1);
        assertThat(course.getCourseId()).isEqualTo("CS101");
        assertThat(course.getName()).isEqualTo("Intro to Programming");
        assertThat(course.getEcts()).isEqualTo(6);

    }

    @Test
    void getProfessorOfOtherUserByIdTest() {
        ResponseEntity<ProfessorDTO> response = restTemplate.withBasicAuth("alice123","password1").
                getForEntity("/professors/{id}", ProfessorDTO.class, 2);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getProfessorThatDoesNotExistTest() {
        ResponseEntity<ProfessorDTO> response = restTemplate.withBasicAuth("alice123","password1").
                getForEntity("/professors/{id}/", ProfessorDTO.class, 9999);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getProfessorListTest() {
        ResponseEntity<List> response = restTemplate.withBasicAuth("alice123","password1").
                getForEntity("/professors", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    void createProfessorTest() {

        ProfessorCreateDTO professor = new ProfessorCreateDTO(
                "TestProfessor",
                "testprof@university.edu",
                "B-101"
        );

        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123","password1").postForEntity("/professors", professor, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<ProfessorDTO> getResponse = restTemplate.withBasicAuth("alice123","password1").getForEntity("/professors/{id}",
                ProfessorDTO.class, 4);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProfessorDTO createdProfessor = getResponse.getBody();

        assertThat(createdProfessor).isNotNull();
        assertThat(createdProfessor.getId()).isEqualTo(4);
        assertThat(createdProfessor.getName()).isEqualTo("TestProfessor");
        assertThat(createdProfessor.getEmail()).isEqualTo("testprof@university.edu");
        assertThat(createdProfessor.getOfficeNumber()).isEqualTo("B-101");
        assertThat(createdProfessor.getCourses().size()).isEqualTo(0);

    }

    @Test
    @DirtiesContext
    void updateProfessorTest() {
        ProfessorUpdateDTO updatedProfessor = new ProfessorUpdateDTO(
                "John Smith",
                "johnsmith@university.edu",
                "B-110"
        );
        HttpEntity<ProfessorUpdateDTO> request = new HttpEntity<>(updatedProfessor);

        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123","password1").
                exchange("/professors/1", HttpMethod.PUT,
                request,Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<ProfessorDTO> getResponse = restTemplate.withBasicAuth("alice123","password1").
                getForEntity("/professors/1", ProfessorDTO.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProfessorDTO professor = getResponse.getBody();
        assertThat(professor).isNotNull();
        assertThat(professor.getId()).isEqualTo(1);
        assertThat(professor.getName()).isEqualTo("John Smith");
        assertThat(professor.getEmail()).isEqualTo("johnsmith@university.edu");
        assertThat(professor.getOfficeNumber()).isEqualTo("B-110");
    }

    @Test
    void updateProfessorOfOtherUserTest() {
        ProfessorUpdateDTO updatedProfessor = new ProfessorUpdateDTO(
                "John Smith",
                "johnsmith@university.edu",
                "B-110"
        );
        HttpEntity<ProfessorUpdateDTO> request = new HttpEntity<>(updatedProfessor);

        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123", "password1").
                exchange("/professors/2", HttpMethod.PUT,
                        request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void updateProfessorThatDoesNotExist(){
            ProfessorUpdateDTO updatedProfessor = new ProfessorUpdateDTO(
                    "John Smith",
                    "johnsmith@university.edu",
                    "B-110"
            );
            HttpEntity<ProfessorUpdateDTO> request = new HttpEntity<>(updatedProfessor);
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123","password1").
                exchange("/professors/999",
                HttpMethod.PUT,request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DirtiesContext
    void deleteProfessorTest() {
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123","password1").
                exchange("/professors/1",
                HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<Professor> getResponse = restTemplate.withBasicAuth("alice123","password1").
                getForEntity("/professors/1",
                Professor.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void deleteProfessorThatDoesNotExistTest() {
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123","password1").
                exchange("/professors/999",
                HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    @DirtiesContext
    void deleteProfessorOfOtherUserTest() {
        ResponseEntity<Void> response = restTemplate.withBasicAuth("alice123","password1").
                exchange("/professors/2",
                        HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }
}
