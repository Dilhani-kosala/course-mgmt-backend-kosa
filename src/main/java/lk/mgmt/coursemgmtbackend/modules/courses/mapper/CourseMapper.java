package lk.mgmt.coursemgmtbackend.modules.courses.mapper;

import lk.mgmt.coursemgmtbackend.modules.courses.dto.CourseDto;
import lk.mgmt.coursemgmtbackend.modules.courses.entity.CourseEntity;
import lk.mgmt.coursemgmtbackend.modules.courses.entity.CoursePrerequisiteEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mappings({
            @Mapping(target = "departmentId", source = "department.id"),
            @Mapping(target = "departmentCode", source = "department.code"),
            @Mapping(target = "departmentName", source = "department.name"),
            @Mapping(target = "prerequisiteIds", ignore = true) // set in @AfterMapping
    })
    CourseDto toDto(CourseEntity e);

    @AfterMapping
    default void fillPrereqIds(CourseEntity e, @MappingTarget CourseDto dto) {
        if (e.getPrerequisites() == null) { dto.setPrerequisiteIds(List.of()); return; }
        dto.setPrerequisiteIds(
                e.getPrerequisites().stream()
                        .map(CoursePrerequisiteEntity::getPrerequisite)
                        .map(CourseEntity::getId)
                        .distinct()
                        .toList()
        );
    }
}
