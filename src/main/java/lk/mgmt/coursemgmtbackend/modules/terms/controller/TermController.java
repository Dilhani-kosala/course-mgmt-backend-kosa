package lk.mgmt.coursemgmtbackend.modules.terms.controller;

import lk.mgmt.coursemgmtbackend.modules.terms.dto.TermDto;
import lk.mgmt.coursemgmtbackend.modules.terms.service.TermService;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/terms")
public class TermController {

    private final TermService service;
    public TermController(TermService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<Page<TermDto>> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.list(q, from, to, PageRequest.of(page, size)));
    }

    @GetMapping("{id}")
    public ResponseEntity<TermDto> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOne(id));
    }
}
