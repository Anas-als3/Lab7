package com.example.lab7.Controller;

import com.example.lab7.ApiResponse.ApiResponse;
import com.example.lab7.Model.Assignment;
import com.example.lab7.Model.Course;
import com.example.lab7.Model.Student;
import com.example.lab7.Model.Teacher;
import com.example.lab7.Service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/get")
    public ResponseEntity<?> getTeachers() {
        return ResponseEntity.status(200).body(teacherService.getTeachers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTeacher(@Valid @RequestBody Teacher teacher, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        int result = teacherService.addTeacher(teacher);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Teacher id already exists"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Teacher added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable String id,
                                           @Valid @RequestBody Teacher teacher,
                                           Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        int result = teacherService.updateTeacher(id, teacher);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Teacher not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Teacher updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable String id) {
        int result = teacherService.deleteTeacher(id);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Teacher not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Teacher deleted successfully"));
    }

    @GetMapping("/courses/{teacherId}")
    public ResponseEntity<?> getTeacherCourses(@PathVariable String teacherId) {
        ArrayList<Course> result = teacherService.getTeacherCourses(teacherId);

        if (result.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No courses found for this teacher"));
        }

        return ResponseEntity.status(200).body(result);
    }

    @PostMapping("/add-assignment/{teacherId}/{courseId}")
    public ResponseEntity<?> teacherAddAssignment(@PathVariable String teacherId,
                                                  @PathVariable String courseId,
                                                  @Valid @RequestBody Assignment assignment,
                                                  Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        int result = teacherService.teacherAddAssignment(teacherId, courseId, assignment);

        if (result == -1) return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
        if (result == -2) return ResponseEntity.status(400).body(new ApiResponse("Teacher does not teach this course"));
        if (result == -3) return ResponseEntity.status(400).body(new ApiResponse("Assignment id already exists"));

        return ResponseEntity.status(200).body(new ApiResponse("Assignment added successfully"));
    }

    @PutMapping("/grade-assignment/{teacherId}/{studentId}/{courseId}/{points}")
    public ResponseEntity<?> gradeAssignment(@PathVariable String teacherId,
                                             @PathVariable String studentId,
                                             @PathVariable String courseId,
                                             @PathVariable int points) {
        int result = teacherService.gradeAssignment(teacherId, studentId, courseId, points);

        if (result == -1) return ResponseEntity.status(400).body(new ApiResponse("Assignment points must be between 0 and 30"));
        if (result == -2) return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
        if (result == -3) return ResponseEntity.status(400).body(new ApiResponse("Teacher does not teach this course"));
        if (result == 0) return ResponseEntity.status(400).body(new ApiResponse("Enrollment not found"));

        return ResponseEntity.status(200).body(new ApiResponse("Assignment graded successfully"));
    }

    @GetMapping("/students/{teacherId}")
    public ResponseEntity<?> getTeacherStudents(@PathVariable String teacherId) {
        ArrayList<Student> result = teacherService.getTeacherStudents(teacherId);

        if (result.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No students found for this teacher"));
        }

        return ResponseEntity.status(200).body(result);
    }
}