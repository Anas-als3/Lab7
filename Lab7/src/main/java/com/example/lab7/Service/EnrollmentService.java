package com.example.lab7.Service;

import com.example.lab7.Model.Course;
import com.example.lab7.Model.Enrollment;
import com.example.lab7.Model.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Getter
@RequiredArgsConstructor
public class EnrollmentService {

    private final StudentService studentService;
    private final CourseService courseService;

    private final ArrayList<Enrollment> enrollmentsList = new ArrayList<>();

    public ArrayList<Enrollment> getEnrollments() {
        return enrollmentsList;
    }

    public int addEnrollment(Enrollment enrollment) {
        for (Enrollment e : enrollmentsList) {
            if (e.getId().equals(enrollment.getId())) {
                return 0;
            }
        }

        enrollmentsList.add(enrollment);
        return 1;
    }

    public int updateEnrollment(String id, Enrollment enrollment) {
        for (int i = 0; i < enrollmentsList.size(); i++) {
            if (enrollmentsList.get(i).getId().equals(id)) {
                enrollment.setId(id);
                enrollmentsList.set(i, enrollment);
                return 1;
            }
        }

        return 0;
    }

    public int deleteEnrollment(String id) {
        for (int i = 0; i < enrollmentsList.size(); i++) {
            if (enrollmentsList.get(i).getId().equals(id)) {
                enrollmentsList.remove(i);
                return 1;
            }
        }

        return 0;
    }

    public int registerStudent(String studentId, String courseId) {
        Student student = studentService.findStudent(studentId);
        Course course = courseService.findCourse(courseId);

        if (student == null) return -1;
        if (course == null) return -2;

        if (!course.isGeneralCourse() && !course.getMajor().equalsIgnoreCase(student.getMajor())) {
            return -3;
        }

        if (student.getLevel() < course.getLevel()) {
            return -4;
        }

        for (Enrollment enrollment : enrollmentsList) {
            if (enrollment.getStudentId().equals(studentId) && enrollment.getCourseId().equals(courseId)) {
                return -5;
            }
        }

        for (Enrollment enrollment : enrollmentsList) {
            if (enrollment.getStudentId().equals(studentId)) {
                Course currentCourse = courseService.findCourse(enrollment.getCourseId());

                if (currentCourse != null &&
                        currentCourse.getDays().equalsIgnoreCase(course.getDays()) &&
                        currentCourse.getClassTime().equalsIgnoreCase(course.getClassTime())) {
                    return -6;
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
            return -7;
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

        return 1;
    }

    public int checkCourseConflict(String studentId, String courseId) {
        Student student = studentService.findStudent(studentId);
        Course newCourse = courseService.findCourse(courseId);

        if (student == null) return -1;
        if (newCourse == null) return -2;

        for (Enrollment enrollment : enrollmentsList) {
            if (enrollment.getStudentId().equals(studentId)) {
                Course currentCourse = courseService.findCourse(enrollment.getCourseId());

                if (currentCourse != null &&
                        currentCourse.getDays().equalsIgnoreCase(newCourse.getDays()) &&
                        currentCourse.getClassTime().equalsIgnoreCase(newCourse.getClassTime())) {
                    return -3;
                }
            }
        }

        return 1;
    }

    public int gradeMidterm(String studentId, String courseId, int points) {
        if (points < 0 || points > 30) {
            return -1;
        }

        Enrollment enrollment = findEnrollmentByStudentAndCourse(studentId, courseId);

        if (enrollment == null) {
            return 0;
        }

        enrollment.setMidtermPoints(points);
        return 1;
    }

    public int gradeFinal(String studentId, String courseId, int points) {
        if (points < 0 || points > 40) {
            return -1;
        }

        Enrollment enrollment = findEnrollmentByStudentAndCourse(studentId, courseId);

        if (enrollment == null) {
            return 0;
        }

        enrollment.setFinalPoints(points);
        return 1;
    }

    public int calculateResult(String studentId, String courseId) {
        Student student = studentService.findStudent(studentId);

        if (student == null) return -1;

        Enrollment enrollment = findEnrollmentByStudentAndCourse(studentId, courseId);

        if (enrollment == null) return -2;

        if (student.getAbsenceRate() >= 25) {
            enrollment.setStatus("Failed");
            return -3;
        }

        int total = enrollment.getAssignmentPoints()
                + enrollment.getMidtermPoints()
                + enrollment.getFinalPoints();

        if (total < 60) {
            enrollment.setStatus("Failed");
            return -4;
        }

        enrollment.setStatus("Passed");
        return total;
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