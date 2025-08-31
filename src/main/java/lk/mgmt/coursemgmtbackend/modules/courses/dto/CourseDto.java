package lk.mgmt.coursemgmtbackend.modules.courses.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CourseDto {
    // getters/setters
    private Long id;
    private String code;
    private String title;
    private String description;
    private Double credits;

    private Long departmentId;
    private String departmentCode;
    private String departmentName;

    private List<Long> prerequisiteIds;

}
