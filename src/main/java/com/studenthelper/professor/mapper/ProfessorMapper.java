package com.studenthelper.professor.mapper;

import com.studenthelper.course.DTO.CourseProfessorDTO;
import com.studenthelper.course.mapper.CourseMapper;
import com.studenthelper.course.model.Course;
import com.studenthelper.professor.DTO.ProfessorCreateDTO;
import com.studenthelper.professor.DTO.ProfessorDTO;
import com.studenthelper.professor.DTO.ProfessorUpdateDTO;
import com.studenthelper.professor.model.Professor;
import com.studenthelper.service.SecurityService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfessorMapper
{
    CourseMapper courseMapper;
    SecurityService securityService;

    ProfessorMapper(CourseMapper courseMapper, SecurityService securityService) {
        this.courseMapper = courseMapper;
        this.securityService = securityService;

    }

    public ProfessorDTO toDTO(Professor professor){
        List<Course> courses = professor.getCourses();
        List<CourseProfessorDTO> coursesDTO = new ArrayList<>();
        for(Course course : courses){
            CourseProfessorDTO courseDTO = courseMapper.toProfessorDTO(course);
            coursesDTO.add(courseDTO);
        }
        String name = professor.getName();
        String email = professor.getEmail();
        String officeNumber = professor.getOfficeNumber();

        if(name ==null){
            name = "";
        }
        if(email == null){
            email = "";
        }
        if(officeNumber == null){
            officeNumber = "";
        }
        return new ProfessorDTO(professor.getId(),name,email,officeNumber,coursesDTO);
    }

    public Professor toEntity(ProfessorDTO professorDTO){

        Professor professor = new Professor(professorDTO.getId(),professorDTO.getName(),
                professorDTO.getEmail(), professorDTO.getOfficeNumber(), null,
                securityService.getCurrentUserId());

        return professor;
    }

    public Professor toEntity(ProfessorCreateDTO professorCreateDTO){
        return new Professor(null,professorCreateDTO.getName(),professorCreateDTO.getEmail(),
                professorCreateDTO.getOfficeNumber(),new ArrayList<>(),securityService.getCurrentUserId());
    }

    public Professor toEntity(ProfessorUpdateDTO professorUpdateDTO){
        return new Professor(null,professorUpdateDTO.getName(),professorUpdateDTO.getEmail(),
                professorUpdateDTO.getOfficeNumber(),null,null);
    }

}
