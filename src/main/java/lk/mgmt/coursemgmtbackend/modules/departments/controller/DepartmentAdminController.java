package lk.mgmt.coursemgmtbackend.modules.departments.controller;

import jakarta.validation.Valid;
import lk.mgmt.coursemgmtbackend.modules.departments.dto.*;
import lk.mgmt.coursemgmtbackend.modules.departments.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/departments")
@PreAuthorize("hasRole('ADMIN')")
public class DepartmentAdminController {

    private final DepartmentService service;
    public DepartmentAdminController(DepartmentService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<DepartmentDto> create(@RequestBody @Valid DepartmentCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<DepartmentDto> update(@PathVariable Long id, @RequestBody @Valid DepartmentUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
