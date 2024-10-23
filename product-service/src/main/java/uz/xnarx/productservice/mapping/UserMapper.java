package uz.xnarx.productservice.mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.xnarx.productservice.entity.Users;
import uz.xnarx.productservice.payload.UserDto;

import java.time.LocalDateTime;

/**
 * @author Samandar Daminov
 * Date: 9/13/2024
 */
@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public static UserDto toDto(Users user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .email(user.getEmail())
                .role(user.getRole())
                .created_at(user.getCreatedAt())
                .updated_at(user.getUpdatedAt())
                .enabled(user.isEnabled())
                .build();
    }
    public Users toEntity(Users user, UserDto userDto) {
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAddress(userDto.getAddress());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(userDto.getRole());
        user.setEnabled(true);
        return user;
    }
}
