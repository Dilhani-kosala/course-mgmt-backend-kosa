package lk.mgmt.coursemgmtbackend.modules.enrollments.mapper;

import lk.mgmt.coursemgmtbackend.modules.enrollments.dto.EnrollmentDto;
import lk.mgmt.coursemgmtbackend.modules.enrollments.entity.EnrollmentEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mappings({
            @Mapping(target = "studentId", source = "student.id"),
            @Mapping(target = "studentName", source = "student.fullName"),
            @Mapping(target = "studentEmail", source = "student.email"),
            @Mapping(target = "offeringId", source = "offering.id"),
            @Mapping(target = "courseCode", source = "offering.course.code"),
            @Mapping(target = "courseTitle", source = "offering.course.title"),
            @Mapping(target = "termCode", source = "offering.term.code"),
            @Mapping(target = "section", source = "offering.section"),
            @Mapping(target = "capacity", source = "offering.capacity")
    })
    EnrollmentDto toDto(EnrollmentEntity e);
}
