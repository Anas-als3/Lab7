package com.example.lab7.Controller;

import com.example.lab7.Model.Assignment;
import com.example.lab7.Model.Teacher;
import com.example.lab7.Service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/get")
    public ResponseEntity<?> getTeachers() {
        return teacherService.getTeachers();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTeacher(@Valid @RequestBody Teacher teacher, Errors errors) {
        return teacherService.addTeacher(teacher, errors);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable String id, @Valid @RequestBody Teacher teacher, Errors errors) {
        return teacherService.updateTeacher(id, teacher, errors);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable String id) {
        return teacherService.deleteTeacher(id);
    }

    @GetMapping("/courses/{teacherId}")
    public ResponseEntity<?> getTeacherCourses(@PathVariable String teacherId) {
        return teacherService.getTeacherCourses(teacherId);
    }

    @PostMapping("/add-assignment/{teacherId}/{courseId}")
    public ResponseEntity<?> teacherAddAssignment(@PathVariable String teacherId,
                                                  @PathVariable String courseId,
                                                  @Valid @RequestBody Assignment assignment,
                                                  Errors errors) {
        return teacherService.teacherAddAssignment(teacherId, courseId, assignment, errors);
    }

    @PutMapping("/grade-assignment/{teacherId}/{studentId}/{courseId}/{points}")
    public ResponseEntity<?> gradeAssignment(@PathVariable String teacherId,
                                             @PathVariable String studentId,
                                             @PathVariable String courseId,
                                             @PathVariable int points) {
        return teacherService.gradeAssignment(teacherId, studentId, courseId, points);
    }

    @GetMapping("/students/{teacherId}")
    public ResponseEntity<?> getTeacherStudents(@PathVariable String teacherId) {
        return teacherService.getTeacherStudents(teacherId);
    }
}