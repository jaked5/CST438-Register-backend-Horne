package com.cst438.controller;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;

import javax.transaction.Transactional;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

    @GetMapping("/student")
    public StudentDTO getStudent( @RequestParam("email") String email) {
        Student student = studentRepository.findByEmail(email);

        if(student == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student email: " + email + " is not valid.");
        }

        return createStudent(student);
    }

    private StudentDTO createStudent(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.student_id = student.getStudent_id();
        studentDTO.studentName = student.getName();
        studentDTO.studentEmail = student.getEmail();
        studentDTO.studentStatus = student.getStatus();
        studentDTO.studentStatusCode = student.getStatusCode();

        return studentDTO;
    }

    // add new student to, and verify no duplicates
    @PostMapping("/student")
    @Transactional
    public StudentDTO addStudent( @RequestBody StudentDTO newStudentDTO) {
        //use findByEmail to verify if exists or not
        Student student = studentRepository.findByEmail(newStudentDTO.studentEmail);

        // throw error if existing student
        if(student != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student: " + newStudentDTO.studentEmail + " already exists, cannot be added again.");
        }

        student = new Student();
        student.setEmail(newStudentDTO.studentEmail);
        student.setName(newStudentDTO.studentName);
        Student result = studentRepository.save(student);
        return createStudent(result);
    }

    @PutMapping("/student/hold/{id}")
    @Transactional
    public void hold( @PathVariable(value="id") int id) {
        Student student = studentRepository.findById(id);

        if(student == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student does not exist: " + id);
        }

        student.setStatus("Hold");
        studentRepository.save(student);
    }

    @PutMapping("/student/release/{id}")
    @Transactional
    public void release( @PathVariable(value="id") int id) {
        Student student = studentRepository.findById(id);

        //
        if(student == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student does not exist: " + id);
        }

        student.setStatus("Valid");
        studentRepository.save(student);
    }

}
