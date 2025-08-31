package lk.mgmt.coursemgmtbackend.modules.users.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreateDto {
    // getters/setters
    @NotBlank @Size(max = 120) private String fullName;
    @Email @NotBlank private String email;
    @NotBlank @Size(min=6, max=100) private String password;
    @NotBlank @Size(max = 32) private String roleCode; // ADMIN | INSTRUCTOR | STUDENT
    @Size(max = 32) private String phone;

}
