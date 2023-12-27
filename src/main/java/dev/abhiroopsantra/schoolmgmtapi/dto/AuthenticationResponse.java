package dev.abhiroopsantra.schoolmgmtapi.dto;

public class AuthenticationResponse {
    public String jwt;
    public String errCode;
    public String errMsg;

    public AuthenticationResponse(String jwt, String errCode, String errMsg) {
        this.jwt = jwt;
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
