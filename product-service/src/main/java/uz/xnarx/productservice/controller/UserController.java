package uz.xnarx.productservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.xnarx.productservice.constant.ProjectEndpoint;
import uz.xnarx.productservice.payload.AuthenticationRequest;
import uz.xnarx.productservice.payload.AuthenticationResponse;
import uz.xnarx.productservice.payload.ProductResponse;
import uz.xnarx.productservice.payload.UserDto;
import uz.xnarx.productservice.service.UserService;
import uz.xnarx.productservice.utils.ApplicationConstants;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register user/ Only User with ADMIN Role can register new user",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = UserDto.class)))
    )
    @PostMapping(value = ProjectEndpoint.USER_REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {
        AuthenticationResponse response = userService.registerUser(userDto);
        if ("Saved".equals(response.getMassage())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // Return 201 Created
        } else if ("Edited".equals(response.getMassage())) {
            return ResponseEntity.ok(response); // Return 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // Return 400 Bad Request or another status
        }
    }

    @Operation(summary = "Authentication with email and Password",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = AuthenticationRequest.class))))
    @PostMapping(value = ProjectEndpoint.USER_AUTH)
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @Operation(summary = "refresh token",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = UserDto.class))))
    @PostMapping(value = ProjectEndpoint.USER_TOKEN)
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        userService.refreshToken(request, response);
    }


    @Operation(summary = "Get all users",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = UserDto.class))))
    @GetMapping(value = ProjectEndpoint.USERS)
    public HttpEntity<?> getAllUser(@RequestParam(value = "page",
            defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                    @RequestParam(value = "size",
                                            defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return ResponseEntity.ok(userService.getAllUser(page, size));
    }

    @Operation(summary = "get one user by Id",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(

                            schema = @Schema(implementation = UserDto.class))))
    @GetMapping(value = ProjectEndpoint.USER_ID)
    HttpEntity<?> getUserById(@PathVariable(value = "id") Long id) {
        ProductResponse apiResponse = userService.getByUserId(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @Operation(summary = "get one user by jwt token in header",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(

                            schema = @Schema(implementation = UserDto.class))))
    @GetMapping(value = ProjectEndpoint.USER_INFO)
    public ResponseEntity<UserDto> getUserByJwt() {
        UserDto userDto = userService.getUserByToken();
        return ResponseEntity.ok(userDto);
    }


    @Operation(summary = "enable user/ it used when Admin restrict the user ",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(

                            schema = @Schema(implementation = UserDto.class))))
    @PutMapping(value = ProjectEndpoint.USER_ENABLE)
    public ResponseEntity<UserDto> enableUser(@PathVariable Long userId) {
        UserDto enabledUser = userService.enableUser(userId);
        return ResponseEntity.ok(enabledUser);
    }

    @Operation(summary = "enable user/ it used when Admin restrict the user ",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(

                            schema = @Schema(implementation = UserDto.class))))
    @PutMapping(value = ProjectEndpoint.USER_DISABLE)
    public ResponseEntity<UserDto> disableUser(@PathVariable Long userId) {
        UserDto disabledUser = userService.disableUser(userId);
        return ResponseEntity.ok(disabledUser);
    }
}
