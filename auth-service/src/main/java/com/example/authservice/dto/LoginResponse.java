package com.example.authservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
public class LoginResponse {

    private Integer id;
    private String email;
    private String fullName;
    private String userName;
    private String message;

}
