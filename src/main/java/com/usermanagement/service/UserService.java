package com.usermanagement.service;

import com.usermanagement.dto.LoginRequest;
import com.usermanagement.dto.PasswordValidationResponse;
import com.usermanagement.dto.RegisterRequest;
import com.usermanagement.dto.UpdateUserRequest;
import com.usermanagement.dto.UserResponse;
import com.usermanagement.entity.User;
import com.usermanagement.exception.UnauthorizedException;
import com.usermanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Business logic layer — validation and orchestration (same rules as the JDBC version).
 */
@Service
public class UserService {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$"
    );

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse registerUser(RegisterRequest request) {
        validateName(request.getName());
        validateEmail(request.getEmail());
        validatePassword(request.getPassword());

        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }

        User user = new User(
                request.getName().trim(),
                email,
                request.getPassword()
        );
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponse loginUser(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        UserResponse response = toResponse(user);
        response.setMessage("Login successful");
        return response;
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        validateName(request.getName());
        validateEmail(request.getEmail());

        String email = request.getEmail().trim().toLowerCase();
        if (!existing.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use: " + email);
        }

        existing.setName(request.getName().trim());
        existing.setEmail(email);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            validatePassword(request.getPassword());
            existing.setPassword(request.getPassword());
        }

        User updated = userRepository.save(existing);
        UserResponse response = toResponse(updated);
        response.setMessage("Profile updated successfully");
        return response;
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public PasswordValidationResponse validatePassword(String password) {
        boolean valid = isValidPassword(password);
        String message = valid
                ? "Password is strong and valid"
                : "Password must be at least 8 characters with uppercase, lowercase, digit, and special character (@#$%^&+=!)";
        return new PasswordValidationResponse(valid, message);
    }

    public boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException(
                    "Password must contain uppercase, lowercase, digit, and special character (@#$%^&+=!).");
        }
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (name.trim().length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }
}
