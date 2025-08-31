package lk.mgmt.coursemgmtbackend.modules.users.service;

import lk.mgmt.coursemgmtbackend.modules.users.dto.*;
import org.springframework.data.domain.*;

public interface UserService {
    UserView create(UserCreateDto dto);
    Page<UserView> list(String q, Pageable pageable);
    UserView findOne(Long id);
    UserView update(Long id, UserUpdateDto dto);
    void delete(Long id);
}
