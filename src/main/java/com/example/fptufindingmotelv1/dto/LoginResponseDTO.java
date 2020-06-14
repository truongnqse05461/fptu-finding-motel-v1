package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponseDTO {
    @JsonProperty("userInfo")
    private LoginDTO loginDTO;
    private String accessToken;
}
