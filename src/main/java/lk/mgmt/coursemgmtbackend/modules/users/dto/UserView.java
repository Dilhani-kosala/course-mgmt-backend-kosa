package lk.mgmt.coursemgmtbackend.modules.users.dto;

import lk.mgmt.coursemgmtbackend.common.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserView {
    // getters/setters
    private Long id;
    private String fullName;
    private String email;
    private String roleCode;
    private UserStatus status;
    private String phone;

}
