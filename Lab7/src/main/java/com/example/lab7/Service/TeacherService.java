package com.example.lab7.Service;

import com.example.lab7.ApiResponse.ApiResponse;
import com.example.lab7.Model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final AssignmentService assignmentService;

    private final ArrayList<Teacher> teachers = new ArrayList<>();

    public ResponseEntity<?> getTeachers() {
        return ResponseEntity.status(200).body(teachers);
    }

    public ResponseEntity<?> addTeacher(Teacher teacher, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        for (Teacher t : teachers) {
            if (t.getId().equals(teacher.getId())) {
                return ResponseEntity.status(400).body(new ApiResponse("Teacher id already exists"));
            }
        }

        teachers.add(teacher);
        return ResponseEntity.status(200).body(new ApiResponse("Teacher added successfully"));
    }

    public ResponseEntity<?> updateTeacher(String id, Teacher teacher, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).getId().equals(id)) {
                teacher.setId(id);
                teachers.set(i, teacher);
                return ResponseEntity.status(200).body(new ApiResponse("Teacher updated successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Teacher not found"));
    }

    public ResponseEntity<?> deleteTeacher(String id) {
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).getId().equals(id)) {
                teachers.remove(i);
                return ResponseEntity.status(200).body(new ApiResponse("Teacher deleted successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Teacher not found"));
    }

    public ResponseEntity<?> getTeacherCourses(String teacherId) {
        ArrayList<Course> result = new ArrayList<>();

        for (Course course : courseService.getCoursesList()) {
            if (course.getTeacherId().equals(teacherId)) {
                result.add(course);
            }
        }

        return ResponseEntity.status(200).body(result);
    }

    public ResponseEntity<?> teacherAddAssignment(String teacherId, String courseId, Assignment assignment, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        Course course = courseService.findCourse(courseId);

        if (course == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
        }

        if (!course.getTeacherId().equals(teacherId)) {
            return ResponseEntity.status(400).body(new ApiResponse("Teacher does not teach this course"));
        }

        assignment.setTeacherId(teacherId);
        assignment.setCourseId(courseId);

        return assignmentService.addAssignment(assignment, errors);
    }

    public ResponseEntity<?> gradeAssignment(String teacherId, String studentId, String courseId, int points) {
        if (points < 0 || points > 30) {
            return ResponseEntity.status(400).body(new ApiResponse("Assignment points must be between 0 and 30"));
        }

        Course course = courseService.findCourse(courseId);

        if (course == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
        }

        if (!course.getTeacherId().equals(teacherId)) {
            return ResponseEntity.status(400).body(new ApiResponse("Teacher does not teach this course"));
        }

        for (Enrollment enrollment : enrollmentService.getEnrollmentsList()) {
            if (enrollment.getStudentId().equals(studentId) && enrollment.getCourseId().equals(courseId)) {
                enrollment.setAssignmentPoints(points);
                return ResponseEntity.status(200).body(new ApiResponse("Assignment graded successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Enrollment not found"));
    }

    public ResponseEntity<?> getTeacherStudents(String teacherId) {
        ArrayList<Student> result = new ArrayList<>();

        for (Course course : courseService.getCoursesList()) {
            if (course.getTeacherId().equals(teacherId)) {
                for (Enrollment enrollment : enrollmentService.getEnrollmentsList()) {
                    if (enrollment.getCourseId().equals(course.getId())) {
                        Student student = studentService.findStudent(enrollment.getStudentId());
                        if (student != null) {
                            result.add(student);
                        }
                    }
                }
            }
        }

        return ResponseEntity.status(200).body(result);
    }

    public Teacher findTeacher(String teacherId) {
        for (Teacher teacher : teachers) {
            if (teacher.getId().equals(teacherId)) {
                return teacher;
            }
        }
        return null;
    }
}