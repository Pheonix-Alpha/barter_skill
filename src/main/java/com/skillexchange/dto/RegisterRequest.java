package com.skillexchange.dto;
import com.skillexchange.model.Role;


import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String email;
    private String password;
    private Role role;


    
}
