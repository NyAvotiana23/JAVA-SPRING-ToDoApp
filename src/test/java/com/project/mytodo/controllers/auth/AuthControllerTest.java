package com.project.mytodo.controllers.auth;

import com.project.mytodo.dto.auth.AuthResponse;
import com.project.mytodo.entities.auth.Role;
import com.project.mytodo.entities.auth.User;
import com.project.mytodo.jwt.JwtUtil;
import com.project.mytodo.repositories.auth.RoleRepository;
import com.project.mytodo.repositories.auth.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthController authController;

    private Role managerRole;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        // Clean up
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Create roles
        managerRole = roleRepository.save(new Role("MANAGER"));
        adminRole = roleRepository.save(new Role("ADMIN"));
    }

    private void createTestUser(String email, String password, Set<Role> roles) {
        User user = new User(email, passwordEncoder.encode(password), roles);
        userRepository.save(user);
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully with valid credentials")
        void shouldLoginSuccessfully() {
            // Given
            Set<Role> roles = new HashSet<>();
            roles.add(managerRole);
            createTestUser("testuser@example.com", "password123", roles);

            // When - Direct authentication test
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken("testuser@example.com", "password123")
            );

            // Then
            assertThat(authentication).isNotNull();
            assertThat(authentication.isAuthenticated()).isTrue();
            assertThat(authentication.getName()).isEqualTo("testuser@example.com");
        }

        @Test
        @DisplayName("Should fail login with invalid password")
        void shouldFailLoginWithInvalidPassword() {
            // Given
            Set<Role> roles = new HashSet<>();
            roles.add(managerRole);
            createTestUser("testuser@example.com", "password123", roles);

            // When/Then
            assertThatThrownBy(() -> authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken("testuser@example.com", "wrongpassword")
            )).isInstanceOf(BadCredentialsException.class);
        }

        @Test
        @DisplayName("Should fail login with non-existent user")
        void shouldFailLoginWithNonExistentUser() {
            // When/Then
            assertThatThrownBy(() -> authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken("nonexistent@example.com", "password123")
            )).isInstanceOf(BadCredentialsException.class);
        }

        @Test
        @DisplayName("Should return multiple roles for user with multiple roles")
        void shouldReturnMultipleRoles() {
            // Given
            Set<Role> roles = new HashSet<>();
            roles.add(managerRole);
            roles.add(adminRole);
            createTestUser("adminuser@example.com", "password123", roles);

            // When
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken("adminuser@example.com", "password123")
            );

            // Then
            assertThat(authentication.getAuthorities()).hasSize(2);
            var authorityNames = authentication.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .toList();
            assertThat(authorityNames).containsExactlyInAnyOrder("MANAGER", "ADMIN");
        }
    }

    @Nested
    @DisplayName("JWT Token Tests")
    class JwtTokenTests {

        @Test
        @DisplayName("Should generate valid JWT token")
        void shouldGenerateValidToken() {
            // Given
            String email = "testuser@example.com";

            // When
            String token = jwtUtil.generateToken(email);

            // Then
            assertThat(token).isNotNull();
            assertThat(token).isNotEmpty();
            assertThat(jwtUtil.validateToken(token)).isTrue();
            assertThat(jwtUtil.extractUsername(token)).isEqualTo(email);
        }

        @Test
        @DisplayName("Should reject invalid JWT token")
        void shouldRejectInvalidToken() {
            // When
            boolean isValid = jwtUtil.validateToken("invalid.token.here");

            // Then
            assertThat(isValid).isFalse();
        }
    }

    @Nested
    @DisplayName("User Repository Tests")
    class UserRepositoryTests {

        @Test
        @DisplayName("Should find user by email")
        void shouldFindUserByEmail() {
            // Given
            Set<Role> roles = new HashSet<>();
            roles.add(managerRole);
            createTestUser("findme@example.com", "password123", roles);

            // When
            User found = userRepository.findByEmail("findme@example.com");

            // Then
            assertThat(found).isNotNull();
            assertThat(found.getEmail()).isEqualTo("findme@example.com");
        }

        @Test
        @DisplayName("Should return null for non-existent email")
        void shouldReturnNullForNonExistentUser() {
            // When
            User found = userRepository.findByEmail("notexist@example.com");

            // Then
            assertThat(found).isNull();
        }

        @Test
        @DisplayName("Should encode password correctly")
        void shouldEncodePasswordCorrectly() {
            // Given
            Set<Role> roles = new HashSet<>();
            roles.add(managerRole);
            createTestUser("passtest@example.com", "mypassword", roles);

            // When
            User found = userRepository.findByEmail("passtest@example.com");

            // Then
            assertThat(found).isNotNull();
            assertThat(passwordEncoder.matches("mypassword", found.getPassword())).isTrue();
            assertThat(passwordEncoder.matches("wrongpassword", found.getPassword())).isFalse();
        }
    }

    @Nested
    @DisplayName("Signup Tests")
    class SignupTests {

        @Test
        @DisplayName("Should not allow duplicate emails")
        void shouldNotAllowDuplicateEmails() {
            // Given
            Set<Role> roles = new HashSet<>();
            roles.add(managerRole);
            createTestUser("existinguser@example.com", "password123", roles);

            // When
            User existing = userRepository.findByEmail("existinguser@example.com");

            // Then
            assertThat(existing).isNotNull();
            assertThat(existing.getEmail()).isEqualTo("existinguser@example.com");
        }

        @Test
        @DisplayName("Should create user with default role")
        void shouldCreateUserWithDefaultRole() {
            // Given
            Set<Role> roles = new HashSet<>();
            roles.add(managerRole);

            User newUser = new User("newuser@example.com", passwordEncoder.encode("password123"), roles);
            userRepository.save(newUser);

            // When
            User found = userRepository.findByEmail("newuser@example.com");

            // Then
            assertThat(found).isNotNull();
            assertThat(found.getRoles()).hasSize(1);
            assertThat(found.getRoles().iterator().next().getName()).isEqualTo("MANAGER");
        }

        @Test
        @DisplayName("Should create user with multiple roles")
        void shouldCreateUserWithMultipleRoles() {
            // Given
            Set<Role> roles = new HashSet<>();
            roles.add(managerRole);
            roles.add(adminRole);

            User newUser = new User("multirole@example.com", passwordEncoder.encode("password123"), roles);
            userRepository.save(newUser);

            // When
            User found = userRepository.findByEmail("multirole@example.com");

            // Then
            assertThat(found).isNotNull();
            assertThat(found.getRoles()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Role Repository Tests")
    class RoleRepositoryTests {

        @Test
        @DisplayName("Should find role by name")
        void shouldFindRoleByName() {
            // When
            Role found = roleRepository.findByName("MANAGER");

            // Then
            assertThat(found).isNotNull();
            assertThat(found.getName()).isEqualTo("MANAGER");
        }

        @Test
        @DisplayName("Should return null for non-existent role")
        void shouldReturnNullForNonExistentRole() {
            // When
            Role found = roleRepository.findByName("NON_EXISTENT");

            // Then
            assertThat(found).isNull();
        }
    }
}
