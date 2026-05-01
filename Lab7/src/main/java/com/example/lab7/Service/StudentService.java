package com.example.lab7.Service;

import com.example.lab7.ApiResponse.ApiResponse;
import com.example.lab7.Model.Student;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;

@Service
@Getter
public class StudentService {

    private final ArrayList<Student> students = new ArrayList<>();

    public ResponseEntity<?> getStudents() {
        return ResponseEntity.status(200).body(students);
    }

    public ResponseEntity<?> addStudent(Student student, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        for (Student s : students) {
            if (s.getId().equals(student.getId())) {
                return ResponseEntity.status(400).body(new ApiResponse("Student id already exists"));
            }
        }

        students.add(student);
        return ResponseEntity.status(200).body(new ApiResponse("Student added successfully"));
    }

    public ResponseEntity<?> updateStudent(String id, Student student, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                student.setId(id);
                students.set(i, student);
                return ResponseEntity.status(200).body(new ApiResponse("Student updated successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
    }

    public ResponseEntity<?> deleteStudent(String id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                students.remove(i);
                return ResponseEntity.status(200).body(new ApiResponse("Student deleted successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
    }

    public ResponseEntity<?> updateGpa(String studentId, double gpa) {
        if (gpa < 0 || gpa > 5) {
            return ResponseEntity.status(400).body(new ApiResponse("GPA must be between 0 and 5"));
        }

        Student student = findStudent(studentId);
        if (student == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        }

        student.setGpa(gpa);
        return ResponseEntity.status(200).body(new ApiResponse("GPA updated successfully"));
    }

    public ResponseEntity<?> updateAbsence(String studentId, double absenceRate) {
        if (absenceRate < 0 || absenceRate > 100) {
            return ResponseEntity.status(400).body(new ApiResponse("Absence rate must be between 0 and 100"));
        }

        Student student = findStudent(studentId);
        if (student == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        }

        student.setAbsenceRate(absenceRate);
        return ResponseEntity.status(200).body(new ApiResponse("Absence updated successfully"));
    }

    public ResponseEntity<?> getStudentsByMajor(String major) {
        ArrayList<Student> result = new ArrayList<>();

        for (Student student : students) {
            if (student.getMajor().equalsIgnoreCase(major)) {
                result.add(student);
            }
        }

        return ResponseEntity.status(200).body(result);
    }

    public ResponseEntity<?> checkStudentStatus(String studentId) {
        Student student = findStudent(studentId);

        if (student == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        }

        if (student.getAbsenceRate() >= 25) {
            return ResponseEntity.status(200).body(new ApiResponse("Student status: Failed because absence rate is 25% or more"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Student status: Active"));
    }

    public Student findStudent(String studentId) {
        for (Student student : students) {
            if (student.getId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }
}