package uz.xnarx.productservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.xnarx.productservice.configuration.JwtService;
import uz.xnarx.productservice.entity.Users;
import uz.xnarx.productservice.exception.BadRequestException;
import uz.xnarx.productservice.exception.NotFoundException;
import uz.xnarx.productservice.mapping.UserMapper;
import uz.xnarx.productservice.payload.AuthenticationRequest;
import uz.xnarx.productservice.payload.AuthenticationResponse;
import uz.xnarx.productservice.payload.ProductResponse;
import uz.xnarx.productservice.payload.UserDto;
import uz.xnarx.productservice.repository.UserRepository;
import uz.xnarx.productservice.utils.CommonUtills;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;


    public AuthenticationResponse registerUser(UserDto userDto) {
        try {
            Users user;

            // Check if ID is provided
            if (userDto.getId() != null) {
                user = userRepository.findById(userDto.getId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found."));

                // Check if the user with the same ID already exists
                if (userRepository.existsById(userDto.getId())) {
                    throw new EntityExistsException("User with ID: " + userDto.getId() + " already exists.");
                }
            } else {
                user = new Users();
            }

            // Check for duplicate email or phone
            if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
                throw new EntityExistsException("Email: " + userDto.getEmail() + " already in use");
            }

            if (userRepository.findByPhone(userDto.getPhone()).isPresent()) {
                throw new EntityExistsException("Phone number: " + userDto.getPhone() + " already in use");
            }
            user.setId(1L);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setAddress(userDto.getAddress());
            user.setEmail(userDto.getEmail());
            user.setPhone(userDto.getPhone());
            user.setCreated_at(LocalDateTime.now());
            user.setUpdated_at(LocalDateTime.now());
            user.setRole(userDto.getRole());
            user.setEnabled(true);
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            userRepository.save(user);

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .message(userDto.getId() != null ? "Edited" : "Saved")
                    .userId(user.getId().toString())
                    .build();
        } catch (EntityExistsException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return AuthenticationResponse.builder()
                    .message("An error occurred: " + e.getMessage())
                    .build();
        }
    }


    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new BadRequestException("Invalid email or password", e);
        }
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!user.isEnabled()){
            throw new BadRequestException("User is disabled");
        }
        var jwtToken = jwtService.generateToken(Map.of("role", user.getRole().name()),user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .message("Authenticated")
                .userId(user.getId().toString())
                .build();
    }

    @Transactional
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadRequestException("Invalid refresh token");
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new NotFoundException("User not found"));
            if (!user.isEnabled()) {
                throw new BadRequestException("User is not enabled");
            }
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Transactional
    public ProductResponse getAllUser(Integer page, Integer size) {
        Page<Users> users;
        try {
            users = userRepository.findAll(CommonUtills.simplePageable(page, size));
            if (users.isEmpty()) {
                return new ProductResponse("No users found", false);
            }

            return new ProductResponse("Success",
                    true,
                    users.getTotalElements(),
                    users.getTotalPages(),
                    users.getContent().stream().map(UserMapper::toDto).collect(Collectors.toList()));
        } catch (Exception e) {
            return new ProductResponse("Get all users failed", false);
        }
    }

    @Transactional
    public ProductResponse getByUserId(Long id) {

        try {
            Users users = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
            return new ProductResponse("User found", true, UserMapper.toDto(users));
        } catch (Exception e) {
            return new ProductResponse("Get by user id failed", false);
        }
    }

    @Transactional
    public UserDto enableUser(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(true);
        user.setUpdated_at(LocalDateTime.now());
        Users updatedUser = userRepository.save(user);
        return objectMapper.convertValue(updatedUser, UserDto.class);
    }

    @Transactional
    public UserDto disableUser(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(false);
        user.setUpdated_at(LocalDateTime.now());
        Users updatedUser = userRepository.save(user);
        return objectMapper.convertValue(updatedUser, UserDto.class);
    }

    public UserDto getUserByToken() {
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UserMapper.toDto(users);
    }

    public AuthenticationResponse editUser(UserDto userDto) {
        // Find the user by ID
        Users existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        // Check if email is already used by another user
        Optional<Users> userWithSameEmail = userRepository.findByEmail(userDto.getEmail());
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(existingUser.getId())) {
            throw new EntityExistsException("Email: " + userDto.getEmail() + " is already in use by another user.");
        }

        // Check if phone is already used by another user
        Optional<Users> userWithSamePhone = userRepository.findByPhone(userDto.getPhone());
        if (userWithSamePhone.isPresent() && !userWithSamePhone.get().getId().equals(existingUser.getId())) {
            throw new EntityExistsException("Phone number: " + userDto.getPhone() + " is already in use by another user.");
        }
        existingUser.setFirstName(userDto.getFirstName() != null ? userDto.getFirstName() : existingUser.getFirstName());
        existingUser.setLastName(userDto.getLastName() != null ? userDto.getLastName() : existingUser.getLastName());
        existingUser.setAddress(userDto.getAddress() != null ? userDto.getAddress() : existingUser.getAddress());
        existingUser.setUpdated_at(LocalDateTime.now());
        // Update password if provided, otherwise keep the existing one
        if (userDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        existingUser.setRole(userDto.getRole() != null ? userDto.getRole() : existingUser.getRole());

        userRepository.save(existingUser);

        // Generate updated JWT tokens
        var jwtToken = jwtService.generateToken(existingUser);
        var refreshToken = jwtService.generateRefreshToken(existingUser);

        // Return a success response
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .message("User details updated successfully.")
                .userId(existingUser.getId().toString())
                .build();
    }

}
