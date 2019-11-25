package com.bn1knb.newsblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationLoginResponseDto {
    private String username;
    private String token;
}
