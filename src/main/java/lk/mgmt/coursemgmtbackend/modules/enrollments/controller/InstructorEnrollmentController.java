package lk.mgmt.coursemgmtbackend.modules.enrollments.controller;

import jakarta.validation.Valid;
import lk.mgmt.coursemgmtbackend.modules.enrollments.dto.*;
import lk.mgmt.coursemgmtbackend.modules.enrollments.service.EnrollmentService;
import lk.mgmt.coursemgmtbackend.security.UserDetailsImpl;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instructor/enrollments")
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorEnrollmentController {

    private final EnrollmentService service;
    public InstructorEnrollmentController(EnrollmentService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<Page<EnrollmentDto>> listMine(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.listByInstructor(user.getId(), PageRequest.of(page, size)));
    }

    @GetMapping("/offering/{offeringId}")
    public ResponseEntity<Page<EnrollmentDto>> listByOffering(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable Long offeringId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.listByOfferingForInstructor(offeringId, user.getId(), PageRequest.of(page, size)));
    }

    @PutMapping("{enrollmentId}")
    public ResponseEntity<EnrollmentDto> update(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable Long enrollmentId,
            @RequestBody @Valid EnrollmentUpdateDto dto
    ) {
        return ResponseEntity.ok(service.updateForInstructor(enrollmentId, user.getId(), dto));
    }
}
