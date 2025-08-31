package lk.mgmt.coursemgmtbackend.modules.offerings.mapper;

import lk.mgmt.coursemgmtbackend.modules.offerings.dto.*;
import lk.mgmt.coursemgmtbackend.modules.offerings.entity.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfferingMapper {

    @Mappings({
            @Mapping(target = "courseId", source = "course.id"),
            @Mapping(target = "courseCode", source = "course.code"),
            @Mapping(target = "courseTitle", source = "course.title"),
            @Mapping(target = "termId", source = "term.id"),
            @Mapping(target = "termCode", source = "term.code"),
            @Mapping(target = "termName", source = "term.name"),
            @Mapping(target = "instructorId", source = "instructor.id"),
            @Mapping(target = "instructorName", source = "instructor.fullName"),
            @Mapping(target = "instructorEmail", source = "instructor.email"),
            @Mapping(target = "schedules", ignore = true)
    })
    OfferingDto toDto(OfferingEntity e);

    @AfterMapping
    default void fillSchedules(OfferingEntity e, @MappingTarget OfferingDto dto) {
        if (e.getSchedules() == null) { dto.setSchedules(List.of()); return; }
        dto.setSchedules(
                e.getSchedules().stream().map(s -> {
                    ScheduleDto d = new ScheduleDto();
                    d.setDayOfWeek(s.getDayOfWeek());
                    d.setStartTime(s.getStartTime());
                    d.setEndTime(s.getEndTime());
                    d.setLocation(s.getLocation());
                    return d;
                }).toList()
        );
    }
}
