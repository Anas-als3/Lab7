package com.example.lab7.Service;

import com.example.lab7.Model.Assignment;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Getter
public class AssignmentService {

    private final ArrayList<Assignment> assignmentsList = new ArrayList<>();

    public ArrayList<Assignment> getAssignments() {
        return assignmentsList;
    }

    public int addAssignment(Assignment assignment) {
        for (Assignment a : assignmentsList) {
            if (a.getId().equals(assignment.getId())) {
                return 0;
            }
        }

        assignmentsList.add(assignment);
        return 1;
    }

    public int updateAssignment(String id, Assignment assignment) {
        for (int i = 0; i < assignmentsList.size(); i++) {
            if (assignmentsList.get(i).getId().equals(id)) {
                assignment.setId(id);
                assignmentsList.set(i, assignment);
                return 1;
            }
        }

        return 0;
    }

    public int deleteAssignment(String id) {
        for (int i = 0; i < assignmentsList.size(); i++) {
            if (assignmentsList.get(i).getId().equals(id)) {
                assignmentsList.remove(i);
                return 1;
            }
        }

        return 0;
    }

    public ArrayList<Assignment> getAssignmentsByCourse(String courseId) {
        ArrayList<Assignment> result = new ArrayList<>();

        for (Assignment assignment : assignmentsList) {
            if (assignment.getCourseId().equals(courseId)) {
                result.add(assignment);
            }
        }

        return result;
    }

    public ArrayList<Assignment> getAssignmentsByTeacher(String teacherId) {
        ArrayList<Assignment> result = new ArrayList<>();

        for (Assignment assignment : assignmentsList) {
            if (assignment.getTeacherId().equals(teacherId)) {
                result.add(assignment);
            }
        }

        return result;
    }

    public int changeDeadline(String assignmentId, String deadline) {
        Assignment assignment = findAssignment(assignmentId);

        if (assignment == null) {
            return 0;
        }

        assignment.setDeadline(deadline);
        return 1;
    }

    public int changeAssignmentMaxPoints(String assignmentId, int points) {
        if (points < 1 || points > 30) {
            return -1;
        }

        Assignment assignment = findAssignment(assignmentId);

        if (assignment == null) {
            return 0;
        }

        assignment.setMaxPoints(points);
        return 1;
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