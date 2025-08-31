package lk.mgmt.coursemgmtbackend.modules.enrollments.controller;

import jakarta.validation.Valid;
import lk.mgmt.coursemgmtbackend.modules.enrollments.dto.*;
import lk.mgmt.coursemgmtbackend.modules.enrollments.service.EnrollmentService;
import lk.mgmt.coursemgmtbackend.security.UserDetailsImpl; // adjust to your principal
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/enrollments")
@PreAuthorize("hasRole('STUDENT')")
public class StudentEnrollmentController {

    private final EnrollmentService service;
    public StudentEnrollmentController(EnrollmentService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<Page<EnrollmentDto>> mine(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.listMine(user.getId(), PageRequest.of(page, size)));
    }

    @PostMapping
    public ResponseEntity<EnrollmentDto> enroll(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestBody @Valid EnrollRequestDto dto
    ) {
        return ResponseEntity.ok(service.enroll(user.getId(), dto.getOfferingId()));
    }

    @DeleteMapping("{enrollmentId}")
    public ResponseEntity<Void> drop(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable Long enrollmentId
    ) {
        service.drop(user.getId(), enrollmentId);
        return ResponseEntity.noContent().build();
    }
}
