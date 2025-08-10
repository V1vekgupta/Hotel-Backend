package learning.hotelbackend.service;
import learning.hotelbackend.exception.RoleAlreadyExistException;
import learning.hotelbackend.exception.UserAlreadyExistsException;
import learning.hotelbackend.model.Role;
import learning.hotelbackend.model.User;
import learning.hotelbackend.repository.RoleRepository;
import learning.hotelbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role theRole) {
        String roleName = "ROLE_" + theRole.getName().toUpperCase();
        Role role = new Role(roleName);
        if (roleRepository.existsByName(roleName)) {
            throw new RoleAlreadyExistException(theRole.getName() + " role already exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        this.removeAllUsersFromRole(roleId);
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role findByName(String name) {
        // 1. CRITICAL FIX: Use orElseThrow for safe Optional handling
        return roleRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Role not found: " + name));
    }

    @Override
    public User removeUserFromRole(Long userId, Long roleId) {
        // 1. CRITICAL FIX: Fetch entities safely first
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new UsernameNotFoundException("Role not found with id: " + roleId));

        if (!role.getUsers().contains(user)) {
            throw new UsernameNotFoundException("User is not assigned to this role");
        }

        role.removeUserFromRole(user);
        roleRepository.save(role);
        return user;
    }

    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        // 1. CRITICAL FIX: Fetch entities safely first
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new UsernameNotFoundException("Role not found with id: " + roleId));

        if (user.getRoles().contains(role)) {
            throw new UserAlreadyExistsException(
                    user.getFirstName() + " is already assigned to the " + role.getName() + " role");
        }

        role.assignRoleToUser(user);
        roleRepository.save(role);
        return user;
    }

    @Override
    public Role removeAllUsersFromRole(Long roleId) {
        // 1. CRITICAL FIX: Handle the Optional safely
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new UsernameNotFoundException("Role not found with id: " + roleId));

        role.removeAllUsersFromRole();
        return roleRepository.save(role);
    }
}