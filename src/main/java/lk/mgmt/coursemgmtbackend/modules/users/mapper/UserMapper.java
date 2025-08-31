package lk.mgmt.coursemgmtbackend.modules.users.mapper;

import lk.mgmt.coursemgmtbackend.modules.users.dto.UserView;
import lk.mgmt.coursemgmtbackend.modules.users.entity.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roleCode", source = "role.code")
    UserView toView(UserEntity e);
}
