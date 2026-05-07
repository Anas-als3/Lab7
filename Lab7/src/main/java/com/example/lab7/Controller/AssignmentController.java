package com.example.lab7.Controller;

import com.example.lab7.ApiResponse.ApiResponse;
import com.example.lab7.Model.Assignment;
import com.example.lab7.Service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping("/get")
    public ResponseEntity<?> getAssignments() {
        return ResponseEntity.status(200).body(assignmentService.getAssignments());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAssignment(@Valid @RequestBody Assignment assignment, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        int result = assignmentService.addAssignment(assignment);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Assignment id already exists"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Assignment added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable String id,
                                              @Valid @RequestBody Assignment assignment,
                                              Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        int result = assignmentService.updateAssignment(id, assignment);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Assignment not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Assignment updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable String id) {
        int result = assignmentService.deleteAssignment(id);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Assignment not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Assignment deleted successfully"));
    }

    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<?> getAssignmentsByCourse(@PathVariable String courseId) {
        ArrayList<Assignment> result = assignmentService.getAssignmentsByCourse(courseId);

        if (result.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No assignments found for this course"));
        }

        return ResponseEntity.status(200).body(result);
    }

    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<?> getAssignmentsByTeacher(@PathVariable String teacherId) {
        ArrayList<Assignment> result = assignmentService.getAssignmentsByTeacher(teacherId);

        if (result.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No assignments found for this teacher"));
        }

        return ResponseEntity.status(200).body(result);
    }

    @PutMapping("/change-deadline/{assignmentId}/{deadline}")
    public ResponseEntity<?> changeDeadline(@PathVariable String assignmentId,
                                            @PathVariable String deadline) {
        int result = assignmentService.changeDeadline(assignmentId, deadline);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Assignment not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Deadline changed successfully"));
    }

    @PutMapping("/change-max-points/{assignmentId}/{points}")
    public ResponseEntity<?> changeAssignmentMaxPoints(@PathVariable String assignmentId,
                                                       @PathVariable int points) {
        int result = assignmentService.changeAssignmentMaxPoints(assignmentId, points);

        if (result == -1) {
            return ResponseEntity.status(400).body(new ApiResponse("Assignment max points must be between 1 and 30"));
        }

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Assignment not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Assignment max points changed successfully"));
    }
}