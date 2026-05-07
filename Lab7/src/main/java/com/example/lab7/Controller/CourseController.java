package com.example.lab7.Controller;

import com.example.lab7.ApiResponse.ApiResponse;
import com.example.lab7.Model.Course;
import com.example.lab7.Service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/get")
    public ResponseEntity<?> getCourses() {
        return ResponseEntity.status(200).body(courseService.getCourses());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@Valid @RequestBody Course course, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        int result = courseService.addCourse(course);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Course id already exists"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Course added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable String id,
                                          @Valid @RequestBody Course course,
                                          Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        int result = courseService.updateCourse(id, course);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Course updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) {
        int result = courseService.deleteCourse(id);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Course deleted successfully"));
    }

    @GetMapping("/by-major/{major}")
    public ResponseEntity<?> getCoursesByMajor(@PathVariable String major) {
        ArrayList<Course> result = courseService.getCoursesByMajor(major);

        if (result.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No courses found for this major"));
        }

        return ResponseEntity.status(200).body(result);
    }

    @GetMapping("/general")
    public ResponseEntity<?> getGeneralCourses() {
        ArrayList<Course> result = courseService.getGeneralCourses();

        if (result.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No general courses found"));
        }

        return ResponseEntity.status(200).body(result);
    }

    @PutMapping("/assign-teacher/{courseId}/{teacherId}")
    public ResponseEntity<?> assignTeacher(@PathVariable String courseId,
                                           @PathVariable String teacherId) {
        int result = courseService.assignTeacher(courseId, teacherId);

        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Teacher assigned successfully"));
    }
}