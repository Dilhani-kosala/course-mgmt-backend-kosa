package lk.mgmt.coursemgmtbackend.modules.offerings.controller;

import jakarta.validation.Valid;
import lk.mgmt.coursemgmtbackend.modules.offerings.dto.*;
import lk.mgmt.coursemgmtbackend.modules.offerings.service.OfferingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/offerings")
@PreAuthorize("hasRole('ADMIN')")
public class OfferingAdminController {

    private final OfferingService service;
    public OfferingAdminController(OfferingService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<OfferingDto> create(@RequestBody @Valid OfferingCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<OfferingDto> update(@PathVariable Long id, @RequestBody @Valid OfferingUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
