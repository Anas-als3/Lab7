package com.example.lab7.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment {

    @NotEmpty(message = "Enrollment id cannot be empty")
    private String id;

    @NotEmpty(message = "Student id cannot be empty")
    private String studentId;

    @NotEmpty(message = "Course id cannot be empty")
    private String courseId;

    @Min(value = 0, message = "Assignment points cannot be less than 0")
    @Max(value = 30, message = "Assignment points cannot be more than 30")
    private int assignmentPoints;

    @Min(value = 0, message = "Midterm points cannot be less than 0")
    @Max(value = 30, message = "Midterm points cannot be more than 30")
    private int midtermPoints;

    @Min(value = 0, message = "Final points cannot be less than 0")
    @Max(value = 40, message = "Final points cannot be more than 40")
    private int finalPoints;

    @NotEmpty(message = "Status cannot be empty")
    @Pattern(regexp = "^(Active|Passed|Failed|Dropped)$", message = "Status must be Active, Passed, Failed, or Dropped")
    private String status;
}