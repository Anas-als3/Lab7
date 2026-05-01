package com.example.lab7.Controller;

import com.example.lab7.Model.Enrollment;
import com.example.lab7.Service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/get")
    public ResponseEntity<?> getEnrollments() {
        return enrollmentService.getEnrollments();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEnrollment(@Valid @RequestBody Enrollment enrollment, Errors errors) {
        return enrollmentService.addEnrollment(enrollment, errors);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEnrollment(@PathVariable String id,
                                              @Valid @RequestBody Enrollment enrollment,
                                              Errors errors) {
        return enrollmentService.updateEnrollment(id, enrollment, errors);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable String id) {
        return enrollmentService.deleteEnrollment(id);
    }

    @PostMapping("/register/{studentId}/{courseId}")
    public ResponseEntity<?> registerStudent(@PathVariable String studentId,
                                             @PathVariable String courseId) {
        return enrollmentService.registerStudent(studentId, courseId);
    }

    @GetMapping("/check-conflict/{studentId}/{courseId}")
    public ResponseEntity<?> checkConflict(@PathVariable String studentId,
                                           @PathVariable String courseId) {
        return enrollmentService.checkCourseConflict(studentId, courseId);
    }

    @PutMapping("/grade-midterm/{studentId}/{courseId}/{points}")
    public ResponseEntity<?> gradeMidterm(@PathVariable String studentId,
                                          @PathVariable String courseId,
                                          @PathVariable int points) {
        return enrollmentService.gradeMidterm(studentId, courseId, points);
    }

    @PutMapping("/grade-final/{studentId}/{courseId}/{points}")
    public ResponseEntity<?> gradeFinal(@PathVariable String studentId,
                                        @PathVariable String courseId,
                                        @PathVariable int points) {
        return enrollmentService.gradeFinal(studentId, courseId, points);
    }

    @GetMapping("/calculate-result/{studentId}/{courseId}")
    public ResponseEntity<?> calculateResult(@PathVariable String studentId,
                                             @PathVariable String courseId) {
        return enrollmentService.calculateResult(studentId, courseId);
    }
}