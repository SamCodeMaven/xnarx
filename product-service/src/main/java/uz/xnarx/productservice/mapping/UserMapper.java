package uz.xnarx.productservice.mapping;

import uz.xnarx.productservice.entity.Users;
import uz.xnarx.productservice.payload.UserDto;

/**
 * @author Samandar Daminov
 * Date: 9/13/2024
 */
public class UserMapper {
    public  static UserDto toDto(Users user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .email(user.getEmail())
                .role(user.getRole())
                .createdDate(user.getCreatedDate())
                .enabled(user.isEnabled())
                .build();
    }
}
