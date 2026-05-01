package com.example.lab7.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @NotEmpty(message = "Course id cannot be empty")
    private String id;

    @NotEmpty(message = "Course name cannot be empty")
    @Size(min = 3, max = 100, message = "Course name must be between 3 and 100 characters")
    private String name;

    @NotEmpty(message = "Major cannot be empty")
    private String major;

    @Min(value = 1, message = "Level must be 1 or higher")
    private int level;

    @Min(value = 1, message = "Hours must be 1 or higher")
    private int hours;

    @NotEmpty(message = "Class time cannot be empty")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d-([01]\\d|2[0-3]):[0-5]\\d$", message = "Class time must be like 10:00-12:00")
    private String classTime;

    @NotEmpty(message = "Days cannot be empty")
    @Pattern(regexp = "^(SUN|MON|TUE|WED|THU)(-(SUN|MON|TUE|WED|THU))*$", message = "Days must be like SUN-TUE or MON-WED")
    private String days;

    @NotEmpty(message = "Teacher id cannot be empty")
    private String teacherId;

    private boolean generalCourse;
}