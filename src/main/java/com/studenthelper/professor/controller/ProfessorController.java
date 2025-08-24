package com.studenthelper.professor.controller;

import com.studenthelper.exceptions.AccessDeniedException;
import com.studenthelper.professor.DTO.ProfessorCreateDTO;
import com.studenthelper.professor.DTO.ProfessorDTO;
import com.studenthelper.professor.DTO.ProfessorUpdateDTO;
import com.studenthelper.professor.exception.ProfessorConflictException;
import com.studenthelper.professor.exception.ProfessorNotFoundException;
import com.studenthelper.professor.mapper.ProfessorMapper;
import com.studenthelper.professor.model.Professor;
import com.studenthelper.professor.services.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/professors")
public class ProfessorController {

    @Autowired
    ProfessorService professorService;

    @Autowired
    ProfessorMapper professorMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> getProfessor(@PathVariable int id) {
        Professor professor = professorService.getProfessorById(id);
        ProfessorDTO professorDTO = professorMapper.toDTO(professor);
        return ResponseEntity.ok(professorDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> getProfessors() {
        List<Professor> professors = professorService.getAllProfessors();
        List<ProfessorDTO> professorDTOs = new ArrayList<>();
        for(Professor professor : professors){
            professorDTOs.add(professorMapper.toDTO(professor));
        }

        return ResponseEntity.ok(professorDTOs);
    }

    @PostMapping
    public ResponseEntity<Void> createProfessor(@RequestBody ProfessorCreateDTO newProfessor) {
        Professor professor = professorMapper.toEntity(newProfessor);
        Professor savedProfessor = professorService.addProfessor(professor);
        Long id = savedProfessor.getId();
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").
                buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProfessor(@PathVariable Long id,@RequestBody ProfessorUpdateDTO updatedProfessor) {
        Professor professor = professorMapper.toEntity(updatedProfessor);
        professorService.updateProfessor(id,professor);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        professorService.deleteProfessor(id);
        return ResponseEntity.noContent().build();
    }

    @ControllerAdvice
    public static class ProfessorExceptionHandler {
        @ExceptionHandler(ProfessorNotFoundException.class)
        public ResponseEntity<String> handleProfessorNotFound(ProfessorNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        @ExceptionHandler(ProfessorConflictException.class)
        public ResponseEntity<String> handleProfessorConflict(ProfessorConflictException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
