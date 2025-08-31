package lk.mgmt.coursemgmtbackend.modules.grades.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TranscriptLineDto {
    private String termCode;
    private String courseCode;
    private String courseTitle;
    private Double credits;
    private String grade;

}
