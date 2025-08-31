package lk.mgmt.coursemgmtbackend.modules.auth.service;

import lk.mgmt.coursemgmtbackend.modules.auth.dto.*;
import lk.mgmt.coursemgmtbackend.modules.users.dto.UserView;

public interface AuthService {
    UserView register(RegisterRequest request);          // default STUDENT
    TokenResponse login(LoginRequest request);
    TokenResponse refresh(String refreshToken);
    UserView me(String email);
}
