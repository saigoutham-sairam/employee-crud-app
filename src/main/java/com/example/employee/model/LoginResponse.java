package com.example.employee.model;

import lombok.Data;

@Data
public class LoginResponse {
    private String sessionId;
    private String message;
}
