package com.example.lab7.Controller;

import com.example.lab7.ApiResponse.ApiResponse;
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
        return ResponseEntity.status(200).body(enrollmentService.getEnrollments());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEnrollment(@Valid @RequestBody Enrollment enrollment, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        int result = enrollmentService.addEnrollment(enrollment);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Enrollment id already exists"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Enrollment added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEnrollment(@PathVariable String id,
                                              @Valid @RequestBody Enrollment enrollment,
                                              Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        int result = enrollmentService.updateEnrollment(id, enrollment);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Enrollment not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Enrollment updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable String id) {
        int result = enrollmentService.deleteEnrollment(id);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Enrollment not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Enrollment deleted successfully"));
    }

    @PostMapping("/register/{studentId}/{courseId}")
    public ResponseEntity<?> registerStudent(@PathVariable String studentId,
                                             @PathVariable String courseId) {
        int result = enrollmentService.registerStudent(studentId, courseId);

        if (result == -1) return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        if (result == -2) return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
        if (result == -3) return ResponseEntity.status(400).body(new ApiResponse("Course is not allowed for student's major"));
        if (result == -4) return ResponseEntity.status(400).body(new ApiResponse("Student level is lower than course level"));
        if (result == -5) return ResponseEntity.status(400).body(new ApiResponse("Student already registered in this course"));
        if (result == -6) return ResponseEntity.status(400).body(new ApiResponse("Course time conflicts with student's schedule"));
        if (result == -7) return ResponseEntity.status(400).body(new ApiResponse("Student exceeded weekly hours limit"));

        return ResponseEntity.status(200).body(new ApiResponse("Student registered successfully"));
    }

    @GetMapping("/check-conflict/{studentId}/{courseId}")
    public ResponseEntity<?> checkConflict(@PathVariable String studentId,
                                           @PathVariable String courseId) {
        int result = enrollmentService.checkCourseConflict(studentId, courseId);

        if (result == -1) return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        if (result == -2) return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
        if (result == -3) return ResponseEntity.status(400).body(new ApiResponse("Course has time conflict"));

        return ResponseEntity.status(200).body(new ApiResponse("No time conflict"));
    }

    @PutMapping("/grade-midterm/{studentId}/{courseId}/{points}")
    public ResponseEntity<?> gradeMidterm(@PathVariable String studentId,
                                          @PathVariable String courseId,
                                          @PathVariable int points) {
        int result = enrollmentService.gradeMidterm(studentId, courseId, points);

        if (result == -1) {
            return ResponseEntity.status(400).body(new ApiResponse("Midterm points must be between 0 and 30"));
        }

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Enrollment not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Midterm graded successfully"));
    }

    @PutMapping("/grade-final/{studentId}/{courseId}/{points}")
    public ResponseEntity<?> gradeFinal(@PathVariable String studentId,
                                        @PathVariable String courseId,
                                        @PathVariable int points) {
        int result = enrollmentService.gradeFinal(studentId, courseId, points);

        if (result == -1) {
            return ResponseEntity.status(400).body(new ApiResponse("Final points must be between 0 and 40"));
        }

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Enrollment not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Final graded successfully"));
    }

    @GetMapping("/calculate-result/{studentId}/{courseId}")
    public ResponseEntity<?> calculateResult(@PathVariable String studentId,
                                             @PathVariable String courseId) {
        int result = enrollmentService.calculateResult(studentId, courseId);

        if (result == -1) return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        if (result == -2) return ResponseEntity.status(400).body(new ApiResponse("Enrollment not found"));
        if (result == -3) return ResponseEntity.status(200).body(new ApiResponse("Student got F because absence rate is 25% or more"));
        if (result == -4) return ResponseEntity.status(200).body(new ApiResponse("Student failed. Total points less than 60"));

        return ResponseEntity.status(200).body(new ApiResponse("Student passed. Total points = " + result));
    }
}