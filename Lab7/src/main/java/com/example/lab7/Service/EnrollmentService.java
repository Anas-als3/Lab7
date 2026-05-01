package com.example.lab7.Service;

import com.example.lab7.ApiResponse.ApiResponse;
import com.example.lab7.Model.Course;
import com.example.lab7.Model.Enrollment;
import com.example.lab7.Model.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;

@Service
@Getter
@RequiredArgsConstructor
public class EnrollmentService {

    private final StudentService studentService;
    private final CourseService courseService;

    private final ArrayList<Enrollment> enrollmentsList = new ArrayList<>();

    public ResponseEntity<?> getEnrollments() {
        return ResponseEntity.status(200).body(enrollmentsList);
    }

    public ResponseEntity<?> addEnrollment(Enrollment enrollment, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        for (Enrollment e : enrollmentsList) {
            if (e.getId().equals(enrollment.getId())) {
                return ResponseEntity.status(400).body(new ApiResponse("Enrollment id already exists"));
            }
        }

        enrollmentsList.add(enrollment);
        return ResponseEntity.status(200).body(new ApiResponse("Enrollment added successfully"));
    }

    public ResponseEntity<?> updateEnrollment(String id, Enrollment enrollment, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        for (int i = 0; i < enrollmentsList.size(); i++) {
            if (enrollmentsList.get(i).getId().equals(id)) {
                enrollment.setId(id);
                enrollmentsList.set(i, enrollment);
                return ResponseEntity.status(200).body(new ApiResponse("Enrollment updated successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Enrollment not found"));
    }

    public ResponseEntity<?> deleteEnrollment(String id) {
        for (int i = 0; i < enrollmentsList.size(); i++) {
            if (enrollmentsList.get(i).getId().equals(id)) {
                enrollmentsList.remove(i);
                return ResponseEntity.status(200).body(new ApiResponse("Enrollment deleted successfully"));
            }
        }

        return ResponseEntity.status(400).body(new ApiResponse("Enrollment not found"));
    }

    public ResponseEntity<?> registerStudent(String studentId, String courseId) {
        Student student = studentService.findStudent(studentId);
        Course course = courseService.findCourse(courseId);

        if (student == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        }

        if (course == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
        }

        if (!course.isGeneralCourse() && !course.getMajor().equalsIgnoreCase(student.getMajor())) {
            return ResponseEntity.status(400).body(new ApiResponse("Course is not allowed for student's major"));
        }

        if (student.getLevel() < course.getLevel()) {
            return ResponseEntity.status(400).body(new ApiResponse("Student level is lower than course level"));
        }

        for (Enrollment enrollment : enrollmentsList) {
            if (enrollment.getStudentId().equals(studentId) && enrollment.getCourseId().equals(courseId)) {
                return ResponseEntity.status(400).body(new ApiResponse("Student already registered in this course"));
            }
        }

        for (Enrollment enrollment : enrollmentsList) {
            if (enrollment.getStudentId().equals(studentId)) {
                Course currentCourse = courseService.findCourse(enrollment.getCourseId());

                if (currentCourse != null &&
                        currentCourse.getDays().equalsIgnoreCase(course.getDays()) &&
                        currentCourse.getClassTime().equalsIgnoreCase(course.getClassTime())) {

                    return ResponseEntity.status(400).body(new ApiResponse("Course time conflicts with student's schedule"));
                }
            }
        }

        int currentHours = 0;

        for (Enrollment enrollment : enrollmentsList) {
            if (enrollment.getStudentId().equals(studentId)) {
                Course currentCourse = courseService.findCourse(enrollment.getCourseId());

                if (currentCourse != null) {
                    currentHours += currentCourse.getHours();
                }
            }
        }

        int maxHours = 20;

        if (student.getGpa() > 4.75) {
            maxHours = 25;
        }

        if (currentHours + course.getHours() > maxHours) {
            return ResponseEntity.status(400).body(new ApiResponse("Student exceeded weekly hours limit"));
        }

        Enrollment enrollment = new Enrollment(
                "E" + (enrollmentsList.size() + 1),
                studentId,
                courseId,
                0,
                0,
                0,
                "Active"
        );

        enrollmentsList.add(enrollment);

        return ResponseEntity.status(200).body(new ApiResponse("Student registered successfully"));
    }

    public ResponseEntity<?> checkCourseConflict(String studentId, String courseId) {
        Student student = studentService.findStudent(studentId);
        Course newCourse = courseService.findCourse(courseId);

        if (student == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        }

        if (newCourse == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Course not found"));
        }

        for (Enrollment enrollment : enrollmentsList) {
            if (enrollment.getStudentId().equals(studentId)) {
                Course currentCourse = courseService.findCourse(enrollment.getCourseId());

                if (currentCourse != null &&
                        currentCourse.getDays().equalsIgnoreCase(newCourse.getDays()) &&
                        currentCourse.getClassTime().equalsIgnoreCase(newCourse.getClassTime())) {

                    return ResponseEntity.status(400).body(new ApiResponse("Course has time conflict"));
                }
            }
        }

        return ResponseEntity.status(200).body(new ApiResponse("No time conflict"));
    }

    public ResponseEntity<?> gradeMidterm(String studentId, String courseId, int points) {
        if (points < 0 || points > 30) {
            return ResponseEntity.status(400).body(new ApiResponse("Midterm points must be between 0 and 30"));
        }

        Enrollment enrollment = findEnrollmentByStudentAndCourse(studentId, courseId);

        if (enrollment == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Enrollment not found"));
        }

        enrollment.setMidtermPoints(points);
        return ResponseEntity.status(200).body(new ApiResponse("Midterm graded successfully"));
    }

    public ResponseEntity<?> gradeFinal(String studentId, String courseId, int points) {
        if (points < 0 || points > 40) {
            return ResponseEntity.status(400).body(new ApiResponse("Final points must be between 0 and 40"));
        }

        Enrollment enrollment = findEnrollmentByStudentAndCourse(studentId, courseId);

        if (enrollment == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Enrollment not found"));
        }

        enrollment.setFinalPoints(points);
        return ResponseEntity.status(200).body(new ApiResponse("Final graded successfully"));
    }

    public ResponseEntity<?> calculateResult(String studentId, String courseId) {
        Student student = studentService.findStudent(studentId);

        if (student == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Student not found"));
        }

        Enrollment enrollment = findEnrollmentByStudentAndCourse(studentId, courseId);

        if (enrollment == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Enrollment not found"));
        }

        if (student.getAbsenceRate() >= 25) {
            enrollment.setStatus("Failed");
            return ResponseEntity.status(200).body(new ApiResponse("Student got F because absence rate is 25% or more"));
        }

        int total = enrollment.getAssignmentPoints()
                + enrollment.getMidtermPoints()
                + enrollment.getFinalPoints();

        if (total < 60) {
            enrollment.setStatus("Failed");
            return ResponseEntity.status(200).body(new ApiResponse("Student failed. Total points = " + total));
        }

        enrollment.setStatus("Passed");
        return ResponseEntity.status(200).body(new ApiResponse("Student passed. Total points = " + total));
    }

    public Enrollment findEnrollmentByStudentAndCourse(String studentId, String courseId) {
        for (Enrollment enrollment : enrollmentsList) {
            if (enrollment.getStudentId().equals(studentId) && enrollment.getCourseId().equals(courseId)) {
                return enrollment;
            }
        }
        return null;
    }
}