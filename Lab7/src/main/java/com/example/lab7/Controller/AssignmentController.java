package com.example.lab7.Controller;

import com.example.lab7.Model.Assignment;
import com.example.lab7.Service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping("/get")
    public ResponseEntity<?> getAssignments() {
        return assignmentService.getAssignments();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAssignment(@Valid @RequestBody Assignment assignment, Errors errors) {
        return assignmentService.addAssignment(assignment, errors);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable String id, @Valid @RequestBody Assignment assignment, Errors errors) {
        return assignmentService.updateAssignment(id, assignment, errors);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable String id) {
        return assignmentService.deleteAssignment(id);
    }

    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<?> getAssignmentsByCourse(@PathVariable String courseId) {
        return assignmentService.getAssignmentsByCourse(courseId);
    }

    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<?> getAssignmentsByTeacher(@PathVariable String teacherId) {
        return assignmentService.getAssignmentsByTeacher(teacherId);
    }

    @PutMapping("/change-deadline/{assignmentId}/{deadline}")
    public ResponseEntity<?> changeDeadline(@PathVariable String assignmentId, @PathVariable String deadline) {
        return assignmentService.changeDeadline(assignmentId, deadline);
    }

    @PutMapping("/change-max-points/{assignmentId}/{points}")
    public ResponseEntity<?> changeAssignmentMaxPoints(@PathVariable String assignmentId, @PathVariable int points) {
        return assignmentService.changeAssignmentMaxPoints(assignmentId, points);
    }
}