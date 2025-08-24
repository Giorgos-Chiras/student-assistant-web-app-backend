package com.studenthelper.professor.services;

import com.studenthelper.professor.model.Professor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProfessorService  {

    Professor getProfessorById(long id);

    Professor addProfessor(Professor professor);


    void updateProfessor(Long id, Professor newProfessor);

    void deleteProfessor(long id);

    List<Professor> getAllProfessors();

}
