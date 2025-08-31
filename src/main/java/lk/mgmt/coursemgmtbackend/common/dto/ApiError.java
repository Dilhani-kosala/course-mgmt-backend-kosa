package lk.mgmt.coursemgmtbackend.common.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiError {
    private String code;
    private String message;

    public ApiError() {}
    public ApiError(String code, String message) { this.code = code; this.message = message; }

}
