package lk.mgmt.coursemgmtbackend.modules.terms.controller;

import jakarta.validation.Valid;
import lk.mgmt.coursemgmtbackend.modules.terms.dto.*;
import lk.mgmt.coursemgmtbackend.modules.terms.service.TermService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/terms")
@PreAuthorize("hasRole('ADMIN')")
public class TermAdminController {

    private final TermService service;
    public TermAdminController(TermService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<TermDto> create(@RequestBody @Valid TermCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<TermDto> update(@PathVariable Long id, @RequestBody @Valid TermUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
