package com.bn1knb.newsblog.dto;

import lombok.Data;

@Data
public class AuthenticationLoginRequestDto {
    private String username;
    private String password;
}
