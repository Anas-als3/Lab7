package com.example.lab7.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {

    @NotEmpty(message = "Teacher id cannot be empty")
    private String id;

    @NotEmpty(message = "Teacher name cannot be empty")
    @Size(min = 3, max = 100, message = "Teacher name must be between 3 and 100 characters")
    private String name;

    @NotEmpty(message = "Specialty cannot be empty")
    private String specialty;
}