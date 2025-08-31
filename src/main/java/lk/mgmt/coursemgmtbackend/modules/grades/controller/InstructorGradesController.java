package lk.mgmt.coursemgmtbackend.modules.grades.controller;

import jakarta.validation.Valid;
import lk.mgmt.coursemgmtbackend.common.enums.Grade;
import lk.mgmt.coursemgmtbackend.modules.grades.dto.*;
import lk.mgmt.coursemgmtbackend.modules.grades.service.GradeService;
import lk.mgmt.coursemgmtbackend.security.UserDetailsImpl; // adjust package
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instructor/grades")
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorGradesController {

    private final GradeService service;
    public InstructorGradesController(GradeService service) { this.service = service; }

    @PostMapping("/set/{enrollmentId}")
    public ResponseEntity<Void> setOne(@AuthenticationPrincipal UserDetailsImpl user,
                                       @PathVariable Long enrollmentId,
                                       @RequestParam Grade grade) {
        service.setGradeByInstructor(user.getId(), enrollmentId, grade);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk")
    public ResponseEntity<Void> bulk(@AuthenticationPrincipal UserDetailsImpl user,
                                     @RequestBody @Valid BulkGradeSetDto dto) {
        service.bulkSetGradesByInstructor(user.getId(), dto);
        return ResponseEntity.noContent().build();
    }
}
