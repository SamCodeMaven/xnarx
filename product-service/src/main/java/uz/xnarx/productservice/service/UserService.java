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
import uz.xnarx.productservice.exception.CustomException;
import uz.xnarx.productservice.exception.NotFoundException;
import uz.xnarx.productservice.mapping.UserMapper;
import uz.xnarx.productservice.payload.AuthenticationRequest;
import uz.xnarx.productservice.payload.AuthenticationResponse;
import uz.xnarx.productservice.payload.ProductResponse;
import uz.xnarx.productservice.payload.UserDto;
import uz.xnarx.productservice.payload.enums.ErrorCode;
import uz.xnarx.productservice.repository.UserRepository;
import uz.xnarx.productservice.service.email.EmailSender;
import uz.xnarx.productservice.service.email.EmailValidator;
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

    private final EmailValidator emailValidator;

    private final EmailSender emailSender;
    private final JwtService jwtService;
    private final VerificationTokenService verificationTokenService;

    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;



    public AuthenticationResponse registerUser(UserDto userDto) {
        boolean isValidEmail= emailValidator.test(userDto.getEmail());

        if (isValidEmail) {
            throw new CustomException(ErrorCode.EMAIL_NOT_VALID);
        }

        try {
            Users user;

            if (userDto.getId() != null) {
                user = userRepository.findById(userDto.getId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found."));

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
            user = userMapper.toEntity(user, userDto);

            String verificationToken=verificationTokenService.saveAndGetVerificationToken(user);
            String link="http://194.31.52.65:8080/api/verification/verify?token="+verificationToken;
            emailSender.send(user.getEmail(), link);

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
        if (!user.isEnabled()) {
            throw new BadRequestException("User is disabled");
        }
        var jwtToken = jwtService.generateToken(Map.of("role", user.getRole().name()), user);
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
        user.setUpdatedAt(LocalDateTime.now());
        Users updatedUser = userRepository.save(user);
        return objectMapper.convertValue(updatedUser, UserDto.class);
    }

    @Transactional
    public UserDto disableUser(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(false);
        user.setUpdatedAt(LocalDateTime.now());
        Users updatedUser = userRepository.save(user);
        return objectMapper.convertValue(updatedUser, UserDto.class);
    }

    public UserDto getUserByToken() {
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UserMapper.toDto(users);
    }

    public AuthenticationResponse editUser(UserDto userDto) {
        Users existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        Optional<Users> userWithSameEmail = userRepository.findByEmail(userDto.getEmail());
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(existingUser.getId())) {
            throw new EntityExistsException("Email: " + userDto.getEmail() + " is already in use by another user.");
        }


        Optional<Users> userWithSamePhone = userRepository.findByPhone(userDto.getPhone());
        if (userWithSamePhone.isPresent() && !userWithSamePhone.get().getId().equals(existingUser.getId())) {
            throw new EntityExistsException("Phone number: " + userDto.getPhone() + " is already in use by another user.");
        }
        existingUser.setFirstName(userDto.getFirstName() != null ? userDto.getFirstName() : existingUser.getFirstName());
        existingUser.setLastName(userDto.getLastName() != null ? userDto.getLastName() : existingUser.getLastName());
        existingUser.setAddress(userDto.getAddress() != null ? userDto.getAddress() : existingUser.getAddress());
        existingUser.setUpdatedAt(LocalDateTime.now());
        if (userDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        existingUser.setRole(userDto.getRole() != null ? userDto.getRole() : existingUser.getRole());

        userRepository.save(existingUser);

        var jwtToken = jwtService.generateToken(existingUser);
        var refreshToken = jwtService.generateRefreshToken(existingUser);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .message("User details updated successfully.")
                .userId(existingUser.getId().toString())
                .build();
    }

}
