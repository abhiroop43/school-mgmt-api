package dev.abhiroopsantra.schoolmgmtapi.dto;

import java.util.HashMap;

public class AuthenticationResponse {
    public HashMap<String, Object> data;
    public String errCode;
    public String errMsg;

    public AuthenticationResponse(HashMap<String, Object> data, String errCode, String errMsg) {
        this.data = data;
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
