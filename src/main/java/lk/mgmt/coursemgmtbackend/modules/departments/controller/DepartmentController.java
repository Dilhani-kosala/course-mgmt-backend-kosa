package lk.mgmt.coursemgmtbackend.modules.departments.controller;

import lk.mgmt.coursemgmtbackend.modules.departments.dto.DepartmentDto;
import lk.mgmt.coursemgmtbackend.modules.departments.service.DepartmentService;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService service;
    public DepartmentController(DepartmentService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<Page<DepartmentDto>> list(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.list(q, PageRequest.of(page, size)));
    }

    @GetMapping("{id}")
    public ResponseEntity<DepartmentDto> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOne(id));
    }
}
