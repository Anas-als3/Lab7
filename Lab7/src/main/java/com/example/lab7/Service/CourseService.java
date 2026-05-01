package com.example.lab7.Service;

import com.example.lab7.ApiResponse.ApiResponse;
import com.example.lab7.Model.Course;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;

@Service
@Getter
public class CourseService {

    private final ArrayList<Course> coursesList = new ArrayList<>();

    public ResponseEntity<?> getCourses() {
        return ResponseEntity.status(200).body(coursesList);
    }

    public ResponseEntity<?> addCourse(Course course, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        for (Course c : coursesList) {
            if (c.getId().equals(course.getId())) {
                return ResponseEntity.status(400).body(new ApiResponse("Course id already exists"));
            }
        }

        coursesList.add(course);
        return ResponseEntity.status(200).body(new ApiResponse("Course added successfully"));
    }

    public ResponseEntity<?> updateCourse(String id, Course course, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        for (int i = 0; i < coursesList.size(); i++) {
            if (coursesList.get(i).getId().equals(id)) {
                course.setId(id);
                coursesList.set(i, course);
                return ResponseEntity.status(200).body(new ApiResponse("Course updated successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
    }

    public ResponseEntity<?> deleteCourse(String id) {
        for (int i = 0; i < coursesList.size(); i++) {
            if (coursesList.get(i).getId().equals(id)) {
                coursesList.remove(i);
                return ResponseEntity.status(200).body(new ApiResponse("Course deleted successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
    }

    public ResponseEntity<?> getCoursesByMajor(String major) {
        ArrayList<Course> result = new ArrayList<>();

        for (Course course : coursesList) {
            if (course.getMajor().equalsIgnoreCase(major) || course.isGeneralCourse()) {
                result.add(course);
            }
        }

        return ResponseEntity.status(200).body(result);
    }

    public ResponseEntity<?> getGeneralCourses() {
        ArrayList<Course> result = new ArrayList<>();

        for (Course course : coursesList) {
            if (course.isGeneralCourse()) {
                result.add(course);
            }
        }

        return ResponseEntity.status(200).body(result);
    }

    public ResponseEntity<?> assignTeacher(String courseId, String teacherId) {
        Course course = findCourse(courseId);

        if (course == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
        }

        course.setTeacherId(teacherId);
        return ResponseEntity.status(200).body(new ApiResponse("Teacher assigned successfully"));
    }

    public Course findCourse(String courseId) {
        for (Course course : coursesList) {
            if (course.getId().equals(courseId)) {
                return course;
            }
        }
        return null;
    }
}