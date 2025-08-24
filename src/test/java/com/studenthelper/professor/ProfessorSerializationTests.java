package com.studenthelper.professor;

import com.studenthelper.course.DTO.CourseProfessorDTO;
import com.studenthelper.professor.DTO.ProfessorDTO;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
public class ProfessorSerializationTests {

    @Autowired
    private JacksonTester<ProfessorDTO> jacksonTester;

    @Autowired
    private JacksonTester<List<ProfessorDTO>> listJacksonTester;

    String professorJson;
    String listJson;


    private List<ProfessorDTO> professors;
    private ProfessorDTO professor;

    @BeforeEach
    void setUp() throws IOException {
       professors = List.of(
                new ProfessorDTO(
                        1L,
                        "John Smith",
                        "john.smith@university.edu",
                        "B-101",
                        List.of(
                                new CourseProfessorDTO(1L, "CS101", "Intro to Programming", 6)
                        )
                ),
                new ProfessorDTO(
                        2L,
                        "Lisa Williams",
                        "lisa.williams@university.edu",
                        "C-202",
                        List.of(
                                new CourseProfessorDTO(2L, "EE202", "Circuit Analysis", 5),
                                new CourseProfessorDTO(4L, "MATH210", "Linear Algebra", 5)
                        )
                ),
                new ProfessorDTO(
                        3L,
                        "Michael Jordan",
                        "michael.jordan@university.edu",
                        "D-303",
                        List.of(
                                new CourseProfessorDTO(3L, "PH301", "Quantum Mechanics", 7)
                        )
                )
        );


        listJson = Files.readString(Paths.get("src/test/resources/list_professor.json"));
        professorJson = Files.readString(Paths.get("src/test/resources/single_professor.json"));
        professor = professors.get(0);
    }



    @Test
    public void serializeProfessor() throws IOException, JSONException {
        JSONAssert.assertEquals(professorJson, jacksonTester.write(professor).getJson(),true);
    }

    @Test
    public void deserializeProfessor() throws IOException, JSONException {

        assertThat(jacksonTester.parseObject(professorJson)).isEqualTo(professor);
    }
    @Test
    public void serializeProfessorList() throws IOException, JSONException {

        JSONAssert.assertEquals(listJson, listJacksonTester.write(professors).getJson(), true);
    }

    @Test
    public void deserializeListProfessor() throws IOException, JSONException {
        assertThat(listJacksonTester.parseObject(listJson)).isEqualTo(professors);
    }
}
