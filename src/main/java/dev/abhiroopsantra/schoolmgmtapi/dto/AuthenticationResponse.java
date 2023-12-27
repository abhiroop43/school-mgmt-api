package dev.abhiroopsantra.schoolmgmtapi.dto;

import lombok.Getter;
import lombok.Setter;


public class AuthenticationResponse {
    public String jwt;

    public AuthenticationResponse(String jwt) {
    }
}
