package lk.mgmt.coursemgmtbackend.modules.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequest {
    // getters/setters
    @NotBlank @Size(max = 120)
    private String fullName;

    @Email @NotBlank
    private String email;

    @NotBlank @Size(min = 6, max = 100)
    private String password;

    // optional
    @Size(max = 32)
    private String phone;

}
