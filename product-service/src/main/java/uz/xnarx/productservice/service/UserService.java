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
import java.util.Date;
import java.util.Map;
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
            Users user = new Users();
            if (userDto.getId() != null) {
                user = userRepository.findById(userDto.getId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found."));
            }
            if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
                throw new EntityExistsException("Email: "+ userDto.getEmail()+ " already in use");
            }
            if (userRepository.findByPhone(userDto.getPhone()) != null) {
                throw new EntityExistsException("Phone number: "+userDto.getPhone()+" already in use");
            }

            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setAddress(userDto.getAddress());
            user.setEmail(userDto.getEmail());
            user.setPhone(userDto.getPhone());
            user.setCreatedDate(new Date());
            user.setRole(userDto.getRole());
            user.setEnabled(true);
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);


            userRepository.save(user);

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .massage(userDto.getId() != null ? "Edited" :"Saved")
                    .userId(user.getId().toString())
                    .build();
        } catch (EntityExistsException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return AuthenticationResponse.builder()
                    .massage(e.getMessage())
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
                .massage("Authenticated")
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
        Users updatedUser = userRepository.save(user);
        return objectMapper.convertValue(updatedUser, UserDto.class);
    }

    @Transactional
    public UserDto disableUser(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(false);
        Users updatedUser = userRepository.save(user);
        return objectMapper.convertValue(updatedUser, UserDto.class);
    }

    public UserDto getUserByToken() {
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UserMapper.toDto(users);
    }
}
