package com.movie.ticketbooking.api;

import com.movie.ticketbooking.model.User;
import com.movie.ticketbooking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users.")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get a specific user by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve details of a specific user using their ID.")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<User> user = userService.getUserById(uuid);

            return user.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid user ID format.");
        }
    }

    // Create a new user
    @PostMapping
    @Operation(summary = "Create a new user", description = "Register a new user in the system.")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    // Update an existing user
    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update details of an existing user.")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User userDetails) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<User> updatedUser = userService.updateUser(uuid, userDetails);

            return updatedUser.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid user ID format.");
        }
    }

    // Delete a user
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Remove a user from the system.")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return userService.deleteUser(uuid)
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid user ID format.");
        }
    }
}
