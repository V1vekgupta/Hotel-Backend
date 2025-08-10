package learning.hotelbackend.controller;
import learning.hotelbackend.exception.RoleAlreadyExistException;
import learning.hotelbackend.model.Role;
import learning.hotelbackend.model.User;
import learning.hotelbackend.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("/all-roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // 1. SECURITY FIX
    public ResponseEntity<List<Role>> getAllRoles() {
        // 2. Corrected HTTP Status Code
        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.OK);
    }

    @PostMapping("/create-new-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // 1. SECURITY FIX
    public ResponseEntity<String> createRole(@RequestBody Role theRole) {
        try {
            roleService.createRole(theRole);
            return ResponseEntity.ok("New role created successfully!");
        } catch (RoleAlreadyExistException re) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(re.getMessage());
        }
    }

    @DeleteMapping("/delete/{roleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // 1. SECURITY FIX
    public ResponseEntity<Void> deleteRole(@PathVariable("roleId") Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build(); // Return 204 No Content on successful deletion
    }

    @PostMapping("/remove-all-users-from-role/{roleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // 1. SECURITY FIX
    public ResponseEntity<Role> removeAllUsersFromRole(@PathVariable("roleId") Long roleId) {
        Role role = roleService.removeAllUsersFromRole(roleId);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/remove-user-from-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // 1. SECURITY FIX
    public ResponseEntity<User> removeUserFromRole(
            @RequestParam("userId") Long userId,
            @RequestParam("roleId") Long roleId) {
        User user = roleService.removeUserFromRole(userId, roleId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/assign-user-to-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // 1. SECURITY FIX
    public ResponseEntity<User> assignUserToRole(
            @RequestParam("userId") Long userId,
            @RequestParam("roleId") Long roleId) {
        User user = roleService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok(user);
    }
}