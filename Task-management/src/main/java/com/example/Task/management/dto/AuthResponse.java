package com.example.Task.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String message;
    
    // Manual builder methods since Lombok isn't working
    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }
    
    public static class AuthResponseBuilder {
        private String token;
        private String message;
        
        public AuthResponseBuilder token(String token) {
            this.token = token;
            return this;
        }
        
        public AuthResponseBuilder message(String message) {
            this.message = message;
            return this;
        }
        
        public AuthResponse build() {
            return new AuthResponse(token, message);
        }
    }
}
