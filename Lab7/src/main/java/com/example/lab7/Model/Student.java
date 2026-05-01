package com.example.lab7.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @NotEmpty(message = "Student id cannot be empty")
    private String id;

    @NotEmpty(message = "Student name cannot be empty")
    @Size(min = 3, max = 100, message = "Student name must be between 3 and 100 characters")
    private String name;

    @NotEmpty(message = "Major cannot be empty")
    private String major;

    @Min(value = 1, message = "Level must be 1 or higher")
    private int level;

    @DecimalMin(value = "0.0", message = "GPA cannot be less than 0")
    @DecimalMax(value = "5.0", message = "GPA cannot be more than 5")
    private double gpa;

    @DecimalMin(value = "0.0", message = "Absence rate cannot be less than 0")
    @DecimalMax(value = "100.0", message = "Absence rate cannot be more than 100")
    private double absenceRate;
}