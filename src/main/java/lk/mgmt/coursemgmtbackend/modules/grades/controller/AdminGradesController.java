package lk.mgmt.coursemgmtbackend.modules.grades.controller;

import lk.mgmt.coursemgmtbackend.common.enums.Grade;
import lk.mgmt.coursemgmtbackend.modules.grades.service.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/grades")
@PreAuthorize("hasRole('ADMIN')")
public class AdminGradesController {

    private final GradeService service;
    public AdminGradesController(GradeService service) { this.service = service; }

    @PostMapping("/set/{enrollmentId}")
    public ResponseEntity<Void> set(@PathVariable Long enrollmentId, @RequestParam Grade grade) {
        service.setGradeByAdmin(enrollmentId, grade);
        return ResponseEntity.noContent().build();
    }
}
