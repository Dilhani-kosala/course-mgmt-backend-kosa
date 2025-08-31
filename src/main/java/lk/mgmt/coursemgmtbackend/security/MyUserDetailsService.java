package lk.mgmt.coursemgmtbackend.security;

import lk.mgmt.coursemgmtbackend.modules.users.entity.UserEntity;
import lk.mgmt.coursemgmtbackend.modules.users.repo.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    public MyUserDetailsService(UserRepository users) { this.users = users; }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity u = users.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsImpl(u);
    }
}
