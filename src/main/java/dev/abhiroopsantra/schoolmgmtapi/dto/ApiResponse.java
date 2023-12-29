package dev.abhiroopsantra.schoolmgmtapi.dto;

import java.util.HashMap;

public class ApiResponse {
    public HashMap<String, Object> data;
    public String                  errCode;
    public String                  errMsg;

    public ApiResponse(HashMap<String, Object> data, String errCode, String errMsg) {
        this.data    = data;
        this.errCode = errCode;
        this.errMsg  = errMsg;
    }
}
