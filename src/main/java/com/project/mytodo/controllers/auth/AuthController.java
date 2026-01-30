package com.project.mytodo.controllers.auth;

import com.project.mytodo.dto.auth.AuthRequest;
import com.project.mytodo.dto.auth.AuthResponse;
import com.project.mytodo.dto.auth.SignUpRequest;
import com.project.mytodo.entities.auth.Role;
import com.project.mytodo.entities.auth.User;
import com.project.mytodo.jwt.JwtUtil;
import com.project.mytodo.repositories.auth.RoleRepository;
import com.project.mytodo.repositories.auth.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        logger.info("Login attempt for user: {}", request.getEmail());

        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                logger.warn("Login failed: Email is empty");
                return ResponseEntity.badRequest().body(AuthResponse.builder()
                        .success(false)
                        .message("Email is required")
                        .build());
            }

            if (request.getPassword() == null || request.getPassword().isBlank()) {
                logger.warn("Login failed: Password is empty for user: {}", request.getEmail());
                return ResponseEntity.badRequest().body(AuthResponse.builder()
                        .success(false)
                        .message("Password is required")
                        .build());
            }

            // Check if user exists
            User user = userRepository.findByEmail(request.getEmail());
            if (user == null) {
                logger.warn("Login failed: User not found: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponse.builder()
                        .success(false)
                        .message("Invalid email or password")
                        .build());
            }

            // Attempt authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Get user roles
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Generate token
            String token = jwtUtil.generateToken(request.getEmail());

            logger.info("Login successful for user: {}", request.getEmail());

            return ResponseEntity.ok(AuthResponse.builder()
                    .success(true)
                    .message("Login successful")
                    .token(token)
                    .email(request.getEmail())
                    .roles(roles)
                    .build());

        } catch (BadCredentialsException e) {
            logger.warn("Login failed: Bad credentials for user: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponse.builder()
                    .success(false)
                    .message("Invalid email or password")
                    .build());

        } catch (DisabledException e) {
            logger.warn("Login failed: Account disabled for user: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponse.builder()
                    .success(false)
                    .message("Account is disabled. Please contact support.")
                    .build());

        } catch (LockedException e) {
            logger.warn("Login failed: Account locked for user: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponse.builder()
                    .success(false)
                    .message("Account is locked. Please try again later.")
                    .build());

        } catch (AuthenticationException e) {
            logger.error("Login failed: Authentication error for user: {} - {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponse.builder()
                    .success(false)
                    .message("Authentication failed: " + e.getMessage())
                    .build());

        } catch (Exception e) {
            logger.error("Login failed: Unexpected error for user: {} - {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
                    .success(false)
                    .message("An unexpected error occurred. Please try again later.")
                    .build());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignUpRequest request) {
        logger.info("Signup attempt for user: {}", request.getEmail());

        try {
            // Check if email already exists
            if (userRepository.findByEmail(request.getEmail()) != null) {
                logger.warn("Signup failed: Email already exists: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(AuthResponse.builder()
                        .success(false)
                        .message("Email already exists")
                        .build());
            }

            // Get roles for the new user
            Set<Role> userRoles = new HashSet<>();
            if (request.getRoles() != null && !request.getRoles().isEmpty()) {
                for (String roleName : request.getRoles()) {
                    Role role = roleRepository.findByName(roleName);
                    if (role == null) {
                        logger.warn("Signup failed: Role not found: {}", roleName);
                        return ResponseEntity.badRequest().body(AuthResponse.builder()
                                .success(false)
                                .message("Role not found: " + roleName)
                                .build());
                    }
                    userRoles.add(role);
                }
            } else {
                // Assign default role (MANAGER) if no roles specified
                Role defaultRole = roleRepository.findByName("MANAGER");
                if (defaultRole == null) {
                    defaultRole = new Role("MANAGER");
                    roleRepository.save(defaultRole);
                }
                userRoles.add(defaultRole);
            }

            // Create and save new user
            User newUser = new User(
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword()),
                    userRoles
            );
            userRepository.save(newUser);

            // Get role names for response
            List<String> roleNames = userRoles.stream()
                    .map(Role::getName)
                    .collect(Collectors.toList());

            // Generate token for immediate login
            String token = jwtUtil.generateToken(request.getEmail());

            logger.info("Signup successful for user: {}", request.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.builder()
                    .success(true)
                    .message("User registered successfully")
                    .token(token)
                    .email(request.getEmail())
                    .roles(roleNames)
                    .build());

        } catch (Exception e) {
            logger.error("Signup failed: Unexpected error for user: {} - {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthResponse.builder()
                    .success(false)
                    .message("Registration failed. Please try again later.")
                    .build());
        }
    }

    @GetMapping("/check")
    public ResponseEntity<AuthResponse> checkAuth(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(AuthResponse.builder()
                    .success(true)
                    .message("Authenticated")
                    .email(authentication.getName())
                    .roles(roles)
                    .build());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponse.builder()
                .success(false)
                .message("Not authenticated")
                .build());
    }
}

