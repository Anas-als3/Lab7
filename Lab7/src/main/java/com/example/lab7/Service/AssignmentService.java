package com.example.lab7.Service;

import com.example.lab7.ApiResponse.ApiResponse;
import com.example.lab7.Model.Assignment;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;

@Service
@Getter
public class AssignmentService {

    private final ArrayList<Assignment> assignmentsList = new ArrayList<>();

    public ResponseEntity<?> getAssignments() {
        return ResponseEntity.status(200).body(assignmentsList);
    }

    public ResponseEntity<?> addAssignment(Assignment assignment, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        for (Assignment a : assignmentsList) {
            if (a.getId().equals(assignment.getId())) {
                return ResponseEntity.status(400).body(new ApiResponse("Assignment id already exists"));
            }
        }

        assignmentsList.add(assignment);
        return ResponseEntity.status(200).body(new ApiResponse("Assignment added successfully"));
    }

    public ResponseEntity<?> updateAssignment(String id, Assignment assignment, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        for (int i = 0; i < assignmentsList.size(); i++) {
            if (assignmentsList.get(i).getId().equals(id)) {
                assignment.setId(id);
                assignmentsList.set(i, assignment);
                return ResponseEntity.status(200).body(new ApiResponse("Assignment updated successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Assignment not found"));
    }

    public ResponseEntity<?> deleteAssignment(String id) {
        for (int i = 0; i < assignmentsList.size(); i++) {
            if (assignmentsList.get(i).getId().equals(id)) {
                assignmentsList.remove(i);
                return ResponseEntity.status(200).body(new ApiResponse("Assignment deleted successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Assignment not found"));
    }

    public ResponseEntity<?> getAssignmentsByCourse(String courseId) {
        ArrayList<Assignment> result = new ArrayList<>();

        for (Assignment assignment : assignmentsList) {
            if (assignment.getCourseId().equals(courseId)) {
                result.add(assignment);
            }
        }

        return ResponseEntity.status(200).body(result);
    }

    public ResponseEntity<?> getAssignmentsByTeacher(String teacherId) {
        ArrayList<Assignment> result = new ArrayList<>();

        for (Assignment assignment : assignmentsList) {
            if (assignment.getTeacherId().equals(teacherId)) {
                result.add(assignment);
            }
        }

        return ResponseEntity.status(200).body(result);
    }

    public ResponseEntity<?> changeDeadline(String assignmentId, String deadline) {
        Assignment assignment = findAssignment(assignmentId);

        if (assignment == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Assignment not found"));
        }

        assignment.setDeadline(deadline);
        return ResponseEntity.status(200).body(new ApiResponse("Deadline changed successfully"));
    }

    public ResponseEntity<?> changeAssignmentMaxPoints(String assignmentId, int points) {
        if (points < 1 || points > 30) {
            return ResponseEntity.status(400).body(new ApiResponse("Assignment max points must be between 1 and 30"));
        }

        Assignment assignment = findAssignment(assignmentId);

        if (assignment == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Assignment not found"));
        }

        assignment.setMaxPoints(points);
        return ResponseEntity.status(200).body(new ApiResponse("Assignment max points changed successfully"));
    }

    public Assignment findAssignment(String assignmentId) {
        for (Assignment assignment : assignmentsList) {
            if (assignment.getId().equals(assignmentId)) {
                return assignment;
            }
        }
        return null;
    }
}