package lk.mgmt.coursemgmtbackend.modules.users.service.impl;

import lk.mgmt.coursemgmtbackend.common.enums.UserStatus;
import lk.mgmt.coursemgmtbackend.modules.users.dto.*;
import lk.mgmt.coursemgmtbackend.modules.users.entity.RoleEntity;
import lk.mgmt.coursemgmtbackend.modules.users.entity.UserEntity;
import lk.mgmt.coursemgmtbackend.modules.users.mapper.UserMapper;
import lk.mgmt.coursemgmtbackend.modules.users.repo.RoleRepository;
import lk.mgmt.coursemgmtbackend.modules.users.repo.UserRepository;
import lk.mgmt.coursemgmtbackend.modules.users.service.UserService;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository users, RoleRepository roles, PasswordEncoder encoder, UserMapper mapper) {
        this.users = users; this.roles = roles; this.encoder = encoder; this.mapper = mapper;
    }

    @Override
    public UserView create(UserCreateDto dto) {
        if (users.existsByEmail(dto.getEmail())) throw new IllegalArgumentException("Email already exists");
        RoleEntity role = roles.findByCode(dto.getRoleCode()).orElseThrow(() -> new IllegalArgumentException("Role not found"));
        UserEntity u = new UserEntity();
        u.setFullName(dto.getFullName());
        u.setEmail(dto.getEmail());
        u.setPasswordHash(encoder.encode(dto.getPassword()));
        u.setRole(role);
        u.setPhone(dto.getPhone());
        u.setStatus(UserStatus.ACTIVE);
        u = users.save(u);
        return mapper.toView(u);
    }

    @Override
    public Page<UserView> list(String q, Pageable pageable) {
        Page<UserEntity> page = (q == null || q.isBlank())
                ? users.findAll(pageable)
                : users.findAll(Example.of(qExample(q)), pageable);
        return page.map(mapper::toView);
    }

    private UserEntity qExample(String q) {
        UserEntity probe = new UserEntity();
        probe.setFullName(q);
        probe.setEmail(q);
        return probe;
    }

    @Override
    public UserView findOne(Long id) {
        return users.findById(id).map(mapper::toView).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public UserView update(Long id, UserUpdateDto dto) {
        UserEntity u = users.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (dto.getFullName() != null) u.setFullName(dto.getFullName());
        if (dto.getPhone() != null) u.setPhone(dto.getPhone());
        if (dto.getStatus() != null) u.setStatus(dto.getStatus());
        return mapper.toView(users.save(u));
    }

    @Override
    public void delete(Long id) {
        users.deleteById(id);
    }
}
