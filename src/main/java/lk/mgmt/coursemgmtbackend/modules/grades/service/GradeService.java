package lk.mgmt.coursemgmtbackend.modules.grades.service;

import lk.mgmt.coursemgmtbackend.modules.grades.dto.*;
import lk.mgmt.coursemgmtbackend.common.enums.Grade;
import org.springframework.data.domain.*;

public interface GradeService
{
    // instructor
    void setGradeByInstructor(Long instructorId, Long enrollmentId, Grade grade);
    void bulkSetGradesByInstructor(Long instructorId, BulkGradeSetDto dto);

    // admin
    void setGradeByAdmin(Long enrollmentId, Grade grade);

    // student transcript
    TranscriptDto getTranscript(Long studentId, Pageable linesPage); // linesPage for limiting lines if needed
    TranscriptDto getTranscriptForTerm(Long studentId, Long termId);
}
