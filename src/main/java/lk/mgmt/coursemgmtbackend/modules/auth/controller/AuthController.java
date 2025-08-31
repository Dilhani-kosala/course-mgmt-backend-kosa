package lk.mgmt.coursemgmtbackend.modules.auth.controller;

import lk.mgmt.coursemgmtbackend.modules.auth.dto.*;
import lk.mgmt.coursemgmtbackend.modules.auth.service.AuthService;
import lk.mgmt.coursemgmtbackend.modules.users.dto.UserView;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    public ResponseEntity<UserView> register(@RequestBody @Valid RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody TokenResponse body) {
        // expects { "refreshToken": "..." }
        return ResponseEntity.ok(authService.refresh(body.getRefreshToken()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserView> me(Authentication auth) {
        return ResponseEntity.ok(authService.me(auth.getName()));
    }
}
