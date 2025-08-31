package lk.mgmt.coursemgmtbackend.modules.users.controller;

import lk.mgmt.coursemgmtbackend.modules.users.dto.*;
import lk.mgmt.coursemgmtbackend.modules.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {
    private final UserService users;
    public UserAdminController(UserService users) { this.users = users; }

    @PostMapping
    public ResponseEntity<UserView> create(@RequestBody @Valid UserCreateDto dto) {
        return ResponseEntity.ok(users.create(dto));
    }

    @GetMapping
    public ResponseEntity<Page<UserView>> list(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(users.list(q, PageRequest.of(page, size)));
    }

    @GetMapping("{id}")
    public ResponseEntity<UserView> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(users.findOne(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<UserView> update(@PathVariable Long id, @RequestBody @Valid UserUpdateDto dto) {
        return ResponseEntity.ok(users.update(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        users.delete(id);
        return ResponseEntity.noContent().build();
    }
}
