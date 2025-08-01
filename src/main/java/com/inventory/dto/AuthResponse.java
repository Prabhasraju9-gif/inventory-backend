package com.inventory.dto;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NoArgsConstructor;
// For successful authentication response
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String role; // Send role to frontend
    private String message;
    private Long userId; // Send userId to frontend

    public AuthResponse(String token, String role, String message, Long userId) {
        this.token=token;
        this.role=role;
        this.message=message;
        this.userId=userId;
    }
}
