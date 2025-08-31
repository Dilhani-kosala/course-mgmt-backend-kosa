package lk.mgmt.coursemgmtbackend.modules.grades.controller;

import lk.mgmt.coursemgmtbackend.modules.grades.dto.TranscriptDto;
import lk.mgmt.coursemgmtbackend.modules.grades.service.GradeService;
import lk.mgmt.coursemgmtbackend.security.UserDetailsImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/transcript")
@PreAuthorize("hasRole('STUDENT')")
public class StudentTranscriptController {

    private final GradeService service;
    public StudentTranscriptController(GradeService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<TranscriptDto> transcript(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(service.getTranscript(user.getId(), PageRequest.of(0, Integer.MAX_VALUE)));
    }

    @GetMapping("/term/{termId}")
    public ResponseEntity<TranscriptDto> transcriptForTerm(@AuthenticationPrincipal UserDetailsImpl user,
                                                           @PathVariable Long termId) {
        return ResponseEntity.ok(service.getTranscriptForTerm(user.getId(), termId));
    }
}
