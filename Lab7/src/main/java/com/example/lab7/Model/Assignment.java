package com.example.lab7.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assignment {

    @NotEmpty(message = "Assignment id cannot be empty")
    private String id;

    @NotEmpty(message = "Course id cannot be empty")
    private String courseId;

    @NotEmpty(message = "Teacher id cannot be empty")
    private String teacherId;

    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotEmpty(message = "Deadline cannot be empty")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Deadline must be like 2026-06-01")
    private String deadline;

    @Min(value = 1, message = "Max points must be 1 or higher")
    @Max(value = 30, message = "Assignments max points cannot be more than 30")
    private int maxPoints;
}