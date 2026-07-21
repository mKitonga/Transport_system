package com.transport.dto.request;

import com.transport.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {

    // Common fields
    
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^(07|01)\\d{8}$",
        message = "Enter a valid Kenyan phone number"
    )
    private String phoneNumber;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters long")
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirm password is required") 
    private String confirmPassword;

    @NotNull(message = "Role is required")
    private Role role;

    // SACCO ADMIN
    private String saccoName;
    private String routeName;

    // SACCO DRIVER
    @Pattern(
        regexp = "^K[A-Z]{2}\\s?\\d{3}[A-Z]$",
        message = "Enter a valid Kenyan number plate (e.g. KDA123A or KDA 123A)"
    )
    private String numberPlate;
    private String matatuName;

    // SCHOOL ADMIN & DRIVER
    private String schoolName;
    private String schoolLocation;

    // PARENT
    private String studentName;
    private String grade;
    private String admissionNumber;
}
