package com.studenthelper.professor.services;

import com.studenthelper.exceptions.AccessDeniedException;
import com.studenthelper.professor.exception.ProfessorNotFoundException;
import com.studenthelper.professor.model.Professor;
import com.studenthelper.professor.repository.ProfessorRepository;
import com.studenthelper.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;

    private final SecurityService securityService;

    ProfessorServiceImpl(ProfessorRepository professorRepository, SecurityService securityService) {
        this.professorRepository = professorRepository;
        this.securityService = securityService;

    }


    @Override
    public Professor getProfessorById(long id) {
        return professorRepository.findAllByIdAndUserId(id, securityService.getCurrentUserId()).
                orElseThrow(ProfessorNotFoundException::new);
    }

    @Override
    public Professor addProfessor(Professor professor) {

        if(professor.getUserId() != securityService.getCurrentUserId()) {
            throw new AccessDeniedException("You do not have permission to add professor");
        }

        return professorRepository.save(professor);
    }

    @Override
    public void updateProfessor(Long id, Professor newProfessor) {
        Professor professor = getProfessorById(id);

        professor.setName(newProfessor.getName());
        professor.setEmail(newProfessor.getEmail());
        professor.setOfficeNumber(newProfessor.getOfficeNumber());

        professorRepository.save(professor);
    }

    @Override
    public void deleteProfessor(long id) {
        Professor professor = getProfessorById(id);
        professorRepository.delete(professor);
    }

    @Override
    public List<Professor> getAllProfessors() {
        return professorRepository.findByUserId(securityService.getCurrentUserId());
    }
}
