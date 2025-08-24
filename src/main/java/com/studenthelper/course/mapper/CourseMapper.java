package com.studenthelper.course.mapper;

import com.studenthelper.course.DTO.*;
import com.studenthelper.course.model.Course;
import com.studenthelper.professor.exception.ProfessorNotFoundException;
import com.studenthelper.professor.model.Professor;
import com.studenthelper.professor.repository.ProfessorRepository;
import com.studenthelper.service.SecurityService;
import com.studenthelper.user.model.User;
import com.studenthelper.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    ProfessorRepository professorRepository;
    private UserRepository userRepository;

    private SecurityService securityService;

    CourseMapper(ProfessorRepository professorRepository, UserRepository userRepository, SecurityService securityService) {
        this.professorRepository = professorRepository;
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    public CourseDTO toDTO(Course course) {

        String professorName = "No Professor";

        if (course.getProfessor() != null && course.getProfessor().getName() != null) {
            professorName = course.getProfessor().getName();
        }

        return new CourseDTO(course.getId(),
                course.getCourseId(),
                course.getName(), course.
                getEcts(),
                professorName);
    }

    public CourseProfessorDTO toProfessorDTO(Course course) {
        return new CourseProfessorDTO(course.getId(),
                course.getCourseId(),
                course.getName(), course.
                getEcts());
    }

    public Course toEntity(CourseUpdateDTO dto) {
        Professor professor;
        try {
            professor = professorRepository.findById(dto.getProfessorId()).get();
        } catch (ProfessorNotFoundException ex) {
            professor = null;
        }

        String courseId = dto.getCourseId();
        String name = dto.getName();
        int ects = dto.getEcts();

        return new Course(null, courseId, name, ects, professor, null, null);

    }

    public Course toEntity(CourseCreateDTO courseCreateDTO) {
        Professor professor;
        User user;
        professor = professorRepository.findById(courseCreateDTO.getProfessorId()).get();
        user = userRepository.findById(securityService.getCurrentUserId()).get();

        Course course = new Course(
                null,
                courseCreateDTO.getCourseId(),
                courseCreateDTO.getName(),
                courseCreateDTO.getEcts(),
                professor,
                user, null
        );
        return  course;
    }


    public static CourseUserDTO toUserDTO(Course course) {
        return new CourseUserDTO(course.getId(), course.getCourseId(), course.getName());
    }

}
