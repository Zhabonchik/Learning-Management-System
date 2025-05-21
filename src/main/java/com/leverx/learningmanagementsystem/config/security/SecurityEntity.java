package com.leverx.learningmanagementsystem.config.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecurityEntity {
    private String username;
    private String password;
}
