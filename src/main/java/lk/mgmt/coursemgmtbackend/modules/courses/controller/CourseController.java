package lk.mgmt.coursemgmtbackend.modules.courses.controller;

import lk.mgmt.coursemgmtbackend.modules.courses.dto.CourseDto;
import lk.mgmt.coursemgmtbackend.modules.courses.service.CourseService;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService service;
    public CourseController(CourseService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<Page<CourseDto>> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.list(q, departmentId, PageRequest.of(page, size)));
    }

    @GetMapping("{id}")
    public ResponseEntity<CourseDto> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOne(id));
    }
}
