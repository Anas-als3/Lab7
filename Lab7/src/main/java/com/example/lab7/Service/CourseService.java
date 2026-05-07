package com.example.lab7.Service;

import com.example.lab7.Model.Course;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Getter
public class CourseService {

    private final ArrayList<Course> coursesList = new ArrayList<>();

    public ArrayList<Course> getCourses() {
        return coursesList;
    }

    public int addCourse(Course course) {
        for (Course c : coursesList) {
            if (c.getId().equals(course.getId())) {
                return 0;
            }
        }

        coursesList.add(course);
        return 1;
    }

    public int updateCourse(String id, Course course) {
        for (int i = 0; i < coursesList.size(); i++) {
            if (coursesList.get(i).getId().equals(id)) {
                course.setId(id);
                coursesList.set(i, course);
                return 1;
            }
        }

        return 0;
    }

    public int deleteCourse(String id) {
        for (int i = 0; i < coursesList.size(); i++) {
            if (coursesList.get(i).getId().equals(id)) {
                coursesList.remove(i);
                return 1;
            }
        }

        return 0;
    }

    public ArrayList<Course> getCoursesByMajor(String major) {
        ArrayList<Course> result = new ArrayList<>();

        for (Course course : coursesList) {
            if (course.getMajor().equalsIgnoreCase(major) || course.isGeneralCourse()) {
                result.add(course);
            }
        }

        return result;
    }

    public ArrayList<Course> getGeneralCourses() {
        ArrayList<Course> result = new ArrayList<>();

        for (Course course : coursesList) {
            if (course.isGeneralCourse()) {
                result.add(course);
            }
        }

        return result;
    }

    public int assignTeacher(String courseId, String teacherId) {
        Course course = findCourse(courseId);

        if (course == null) {
            return 0;
        }

        course.setTeacherId(teacherId);
        return 1;
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