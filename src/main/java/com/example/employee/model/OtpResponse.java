package com.example.employee.model;

import lombok.Data;

@Data
public class OtpResponse {
    private boolean isVerified;
    private String message;
}
