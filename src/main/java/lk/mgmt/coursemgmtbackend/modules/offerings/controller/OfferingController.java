package lk.mgmt.coursemgmtbackend.modules.offerings.controller;

import lk.mgmt.coursemgmtbackend.modules.offerings.dto.OfferingDto;
import lk.mgmt.coursemgmtbackend.modules.offerings.service.OfferingService;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/offerings")
public class OfferingController {

    private final OfferingService service;
    public OfferingController(OfferingService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<Page<OfferingDto>> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long termId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long instructorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.list(q, termId, courseId, instructorId, PageRequest.of(page, size)));
    }

    @GetMapping("{id}")
    public ResponseEntity<OfferingDto> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOne(id));
    }
}
