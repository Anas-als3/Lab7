package com.example.lab7.Controller;

import com.example.lab7.ApiResponse.ApiResponse;
import com.example.lab7.Model.Student;
import com.example.lab7.Service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/get")
    public ResponseEntity<?> getStudents() {
        return ResponseEntity.status(200).body(studentService.getStudents());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStudent(@Valid @RequestBody Student student, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        int result = studentService.addStudent(student);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Student id already exists"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Student added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id,
                                           @Valid @RequestBody Student student,
                                           Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        int result = studentService.updateStudent(id, student);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Student updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        int result = studentService.deleteStudent(id);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Student deleted successfully"));
    }

    @PutMapping("/update-gpa/{studentId}/{gpa}")
    public ResponseEntity<?> updateGpa(@PathVariable String studentId,
                                       @PathVariable double gpa) {
        int result = studentService.updateGpa(studentId, gpa);

        if (result == -1) {
            return ResponseEntity.status(400).body(new ApiResponse("GPA must be between 0 and 5"));
        }

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("GPA updated successfully"));
    }

    @PutMapping("/update-absence/{studentId}/{absenceRate}")
    public ResponseEntity<?> updateAbsence(@PathVariable String studentId,
                                           @PathVariable double absenceRate) {
        int result = studentService.updateAbsence(studentId, absenceRate);

        if (result == -1) {
            return ResponseEntity.status(400).body(new ApiResponse("Absence rate must be between 0 and 100"));
        }

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Absence updated successfully"));
    }

    @GetMapping("/by-major/{major}")
    public ResponseEntity<?> getStudentsByMajor(@PathVariable String major) {
        ArrayList<Student> result = studentService.getStudentsByMajor(major);

        if (result.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No students found for this major"));
        }

        return ResponseEntity.status(200).body(result);
    }

    @GetMapping("/status/{studentId}")
    public ResponseEntity<?> checkStudentStatus(@PathVariable String studentId) {
        int result = studentService.checkStudentStatus(studentId);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        }

        if (result == -1) {
            return ResponseEntity.status(200).body(new ApiResponse("Student status: Failed because absence rate is 25% or more"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Student status: Active"));
    }
}