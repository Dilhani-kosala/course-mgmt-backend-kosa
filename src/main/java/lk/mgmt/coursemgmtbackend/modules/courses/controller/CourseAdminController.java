package lk.mgmt.coursemgmtbackend.modules.courses.controller;

import jakarta.validation.Valid;
import lk.mgmt.coursemgmtbackend.modules.courses.dto.*;
import lk.mgmt.coursemgmtbackend.modules.courses.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
@PreAuthorize("hasRole('ADMIN')")
public class CourseAdminController {

    private final CourseService service;
    public CourseAdminController(CourseService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<CourseDto> create(@RequestBody @Valid CourseCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<CourseDto> update(@PathVariable Long id, @RequestBody @Valid CourseUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
