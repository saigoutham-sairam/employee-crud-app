package com.example.employee.model;

import lombok.Data;

@Data
public class OtpRequest {
    private String otp;
    private String sessionId;
}
