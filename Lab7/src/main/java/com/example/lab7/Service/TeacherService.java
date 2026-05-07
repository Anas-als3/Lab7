package com.example.lab7.Service;

import com.example.lab7.Model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final AssignmentService assignmentService;

    private final ArrayList<Teacher> teachers = new ArrayList<>();

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public int addTeacher(Teacher teacher) {
        for (Teacher t : teachers) {
            if (t.getId().equals(teacher.getId())) {
                return 0;
            }
        }

        teachers.add(teacher);
        return 1;
    }

    public int updateTeacher(String id, Teacher teacher) {
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).getId().equals(id)) {
                teacher.setId(id);
                teachers.set(i, teacher);
                return 1;
            }
        }

        return 0;
    }

    public int deleteTeacher(String id) {
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).getId().equals(id)) {
                teachers.remove(i);
                return 1;
            }
        }

        return 0;
    }

    public ArrayList<Course> getTeacherCourses(String teacherId) {
        ArrayList<Course> result = new ArrayList<>();

        for (Course course : courseService.getCoursesList()) {
            if (course.getTeacherId().equals(teacherId)) {
                result.add(course);
            }
        }

        return result;
    }

    public int teacherAddAssignment(String teacherId, String courseId, Assignment assignment) {
        Course course = courseService.findCourse(courseId);

        if (course == null) {
            return -1;
        }

        if (!course.getTeacherId().equals(teacherId)) {
            return -2;
        }

        assignment.setTeacherId(teacherId);
        assignment.setCourseId(courseId);

        int result = assignmentService.addAssignment(assignment);

        if (result == 0) {
            return -3;
        }

        return 1;
    }

    public int gradeAssignment(String teacherId, String studentId, String courseId, int points) {
        if (points < 0 || points > 30) {
            return -1;
        }

        Course course = courseService.findCourse(courseId);

        if (course == null) {
            return -2;
        }

        if (!course.getTeacherId().equals(teacherId)) {
            return -3;
        }

        for (Enrollment enrollment : enrollmentService.getEnrollmentsList()) {
            if (enrollment.getStudentId().equals(studentId) && enrollment.getCourseId().equals(courseId)) {
                enrollment.setAssignmentPoints(points);
                return 1;
            }
        }

        return 0;
    }

    public ArrayList<Student> getTeacherStudents(String teacherId) {
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

        return result;
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