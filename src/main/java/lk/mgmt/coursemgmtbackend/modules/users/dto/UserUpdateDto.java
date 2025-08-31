package lk.mgmt.coursemgmtbackend.modules.users.dto;

import lk.mgmt.coursemgmtbackend.common.enums.UserStatus;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdateDto {
    // getters/setters
    @Size(max = 120) private String fullName;
    @Size(max = 32) private String phone;
    private UserStatus status; // ACTIVE or DISABLED

}
