package lk.mgmt.coursemgmtbackend.modules.auth.service.impl;

import lk.mgmt.coursemgmtbackend.common.enums.UserStatus;
import lk.mgmt.coursemgmtbackend.modules.auth.dto.*;
import lk.mgmt.coursemgmtbackend.modules.auth.service.AuthService;
import lk.mgmt.coursemgmtbackend.modules.users.dto.UserView;
import lk.mgmt.coursemgmtbackend.modules.users.entity.RoleEntity;
import lk.mgmt.coursemgmtbackend.modules.users.entity.UserEntity;
import lk.mgmt.coursemgmtbackend.modules.users.mapper.UserMapper;
import lk.mgmt.coursemgmtbackend.modules.users.repo.RoleRepository;
import lk.mgmt.coursemgmtbackend.modules.users.repo.UserRepository;
import lk.mgmt.coursemgmtbackend.security.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authManager;
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final UserMapper mapper;

    public AuthServiceImpl(AuthenticationManager am, UserRepository users, RoleRepository roles,
                           PasswordEncoder encoder, JwtService jwt, UserMapper mapper) {
        this.authManager = am; this.users = users; this.roles = roles;
        this.encoder = encoder; this.jwt = jwt; this.mapper = mapper;
    }

    @Override
    public UserView register(RegisterRequest r) {
        if (users.existsByEmail(r.getEmail())) throw new IllegalArgumentException("Email already exists");
        RoleEntity student = roles.findByCode("STUDENT").orElseThrow(() -> new IllegalStateException("Role STUDENT not found"));
        UserEntity u = new UserEntity();
        u.setFullName(r.getFullName());
        u.setEmail(r.getEmail());
        u.setPasswordHash(encoder.encode(r.getPassword()));
        u.setRole(student);
        u.setPhone(r.getPhone());
        u.setStatus(UserStatus.ACTIVE);
        return mapper.toView(users.save(u));
    }

    @Override
    public TokenResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        UserEntity u = users.findByEmail(req.getEmail()).orElseThrow();
        String access = jwt.generateAccessToken(u.getEmail(), Map.of("role", u.getRole().getCode(), "uid", u.getId()));
        String refresh = jwt.generateRefreshToken(u.getEmail());
        return new TokenResponse(access, refresh);
    }

    @Override
    public TokenResponse refresh(String refreshToken) {
        Claims claims = jwt.parse(refreshToken).getPayload();
        if (!"refresh".equals(claims.get("typ"))) throw new IllegalArgumentException("Invalid refresh token");
        String email = claims.getSubject();
        UserEntity u = users.findByEmail(email).orElseThrow();
        String access = jwt.generateAccessToken(u.getEmail(), Map.of("role", u.getRole().getCode(), "uid", u.getId()));
        String newRefresh = jwt.generateRefreshToken(u.getEmail()); // rotate
        return new TokenResponse(access, newRefresh);
    }

    @Override
    public UserView me(String email) {
        return users.findByEmail(email).map(mapper::toView).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
