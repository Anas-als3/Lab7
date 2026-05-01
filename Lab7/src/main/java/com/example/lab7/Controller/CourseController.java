package com.example.lab7.Controller;

import com.example.lab7.Model.Course;
import com.example.lab7.Service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/get")
    public ResponseEntity<?> getCourses() {
        return courseService.getCourses();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@Valid @RequestBody Course course, Errors errors) {
        return courseService.addCourse(course, errors);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable String id, @Valid @RequestBody Course course, Errors errors) {
        return courseService.updateCourse(id, course, errors);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) {
        return courseService.deleteCourse(id);
    }

    @GetMapping("/by-major/{major}")
    public ResponseEntity<?> getCoursesByMajor(@PathVariable String major) {
        return courseService.getCoursesByMajor(major);
    }

    @GetMapping("/general")
    public ResponseEntity<?> getGeneralCourses() {
        return courseService.getGeneralCourses();
    }

    @PutMapping("/assign-teacher/{courseId}/{teacherId}")
    public ResponseEntity<?> assignTeacher(@PathVariable String courseId, @PathVariable String teacherId) {
        return courseService.assignTeacher(courseId, teacherId);
    }
}