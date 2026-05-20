package com.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordValidationRequest {

    @NotBlank(message = "Password is required")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
