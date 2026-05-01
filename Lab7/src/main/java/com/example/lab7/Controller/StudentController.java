package com.example.lab7.Controller;

import com.example.lab7.Model.Student;
import com.example.lab7.Service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/get")
    public ResponseEntity<?> getStudents() {
        return studentService.getStudents();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStudent(@Valid @RequestBody Student student, Errors errors) {
        return studentService.addStudent(student, errors);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @Valid @RequestBody Student student, Errors errors) {
        return studentService.updateStudent(id, student, errors);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        return studentService.deleteStudent(id);
    }

    @PutMapping("/update-gpa/{studentId}/{gpa}")
    public ResponseEntity<?> updateGpa(@PathVariable String studentId, @PathVariable double gpa) {
        return studentService.updateGpa(studentId, gpa);
    }

    @PutMapping("/update-absence/{studentId}/{absenceRate}")
    public ResponseEntity<?> updateAbsence(@PathVariable String studentId, @PathVariable double absenceRate) {
        return studentService.updateAbsence(studentId, absenceRate);
    }

    @GetMapping("/by-major/{major}")
    public ResponseEntity<?> getStudentsByMajor(@PathVariable String major) {
        return studentService.getStudentsByMajor(major);
    }

    @GetMapping("/status/{studentId}")
    public ResponseEntity<?> checkStudentStatus(@PathVariable String studentId) {
        return studentService.checkStudentStatus(studentId);
    }
}