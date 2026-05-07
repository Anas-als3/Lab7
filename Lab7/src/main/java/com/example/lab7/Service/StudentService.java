package com.example.lab7.Service;

import com.example.lab7.Model.Student;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Getter
public class StudentService {

    private final ArrayList<Student> students = new ArrayList<>();

    public ArrayList<Student> getStudents() {
        return students;
    }

    public int addStudent(Student student) {
        for (Student s : students) {
            if (s.getId().equals(student.getId())) {
                return 0;
            }
        }

        students.add(student);
        return 1;
    }

    public int updateStudent(String id, Student student) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                student.setId(id);
                students.set(i, student);
                return 1;
            }
        }

        return 0;
    }

    public int deleteStudent(String id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                students.remove(i);
                return 1;
            }
        }

        return 0;
    }

    public int updateGpa(String studentId, double gpa) {
        if (gpa < 0 || gpa > 5) {
            return -1;
        }

        Student student = findStudent(studentId);

        if (student == null) {
            return 0;
        }

        student.setGpa(gpa);
        return 1;
    }

    public int updateAbsence(String studentId, double absenceRate) {
        if (absenceRate < 0 || absenceRate > 100) {
            return -1;
        }

        Student student = findStudent(studentId);

        if (student == null) {
            return 0;
        }

        student.setAbsenceRate(absenceRate);
        return 1;
    }

    public ArrayList<Student> getStudentsByMajor(String major) {
        ArrayList<Student> result = new ArrayList<>();

        for (Student student : students) {
            if (student.getMajor().equalsIgnoreCase(major)) {
                result.add(student);
            }
        }

        return result;
    }

    public int checkStudentStatus(String studentId) {
        Student student = findStudent(studentId);

        if (student == null) {
            return 0;
        }

        if (student.getAbsenceRate() >= 25) {
            return -1;
        }

        return 1;
    }

    public Student findStudent(String studentId) {
        for (Student student : students) {
            if (student.getId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }
}