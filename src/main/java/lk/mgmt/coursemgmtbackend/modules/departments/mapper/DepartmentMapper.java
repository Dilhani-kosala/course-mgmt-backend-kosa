package lk.mgmt.coursemgmtbackend.modules.departments.mapper;

import lk.mgmt.coursemgmtbackend.modules.departments.dto.DepartmentDto;
import lk.mgmt.coursemgmtbackend.modules.departments.entity.DepartmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentDto toDto(DepartmentEntity e);
}
