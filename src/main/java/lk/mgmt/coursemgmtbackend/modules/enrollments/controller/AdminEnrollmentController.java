package lk.mgmt.coursemgmtbackend.modules.enrollments.controller;

import jakarta.validation.Valid;
import lk.mgmt.coursemgmtbackend.modules.enrollments.dto.*;
import lk.mgmt.coursemgmtbackend.modules.enrollments.service.EnrollmentService;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/enrollments")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEnrollmentController {

    private final EnrollmentService service;
    public AdminEnrollmentController(EnrollmentService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<Page<EnrollmentDto>> list(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.listAll(PageRequest.of(page, size)));
    }

    @GetMapping("/offering/{offeringId}")
    public ResponseEntity<Page<EnrollmentDto>> listByOffering(@PathVariable Long offeringId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.listByOffering(offeringId, PageRequest.of(page, size)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<EnrollmentDto>> listByStudent(@PathVariable Long studentId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.listByStudent(studentId, PageRequest.of(page, size)));
    }

    @PostMapping
    public ResponseEntity<EnrollmentDto> create(@RequestParam Long studentId, @RequestParam Long offeringId) {
        return ResponseEntity.ok(service.adminCreate(studentId, offeringId));
    }

    @PutMapping("{enrollmentId}")
    public ResponseEntity<EnrollmentDto> update(@PathVariable Long enrollmentId,
                                                @RequestBody @Valid EnrollmentUpdateDto dto) {
        return ResponseEntity.ok(service.adminUpdate(enrollmentId, dto));
    }

    @DeleteMapping("{enrollmentId}")
    public ResponseEntity<Void> delete(@PathVariable Long enrollmentId) {
        service.adminDelete(enrollmentId);
        return ResponseEntity.noContent().build();
    }
}
