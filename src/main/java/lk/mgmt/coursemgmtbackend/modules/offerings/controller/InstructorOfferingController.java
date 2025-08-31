package lk.mgmt.coursemgmtbackend.modules.offerings.controller;

import jakarta.validation.Valid;
import lk.mgmt.coursemgmtbackend.modules.offerings.dto.*;
import lk.mgmt.coursemgmtbackend.modules.offerings.service.OfferingService;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// Replace 'UserDetailsImpl' with your actual principal class that exposes getId()
import lk.mgmt.coursemgmtbackend.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/instructor/offerings")
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorOfferingController {

    private final OfferingService service;
    public InstructorOfferingController(OfferingService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<Page<OfferingDto>> listMine(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long termId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                service.listMine(q, termId, courseId, PageRequest.of(page, size), user.getId())
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<OfferingDto> getMine(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getMine(id, user.getId()));
    }

    @PutMapping("{id}")
    public ResponseEntity<OfferingDto> updateMine(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable Long id,
            @RequestBody @Valid InstructorOfferingUpdateDto dto
    ) {
        return ResponseEntity.ok(service.updateByInstructor(id, dto, user.getId()));
    }
}
