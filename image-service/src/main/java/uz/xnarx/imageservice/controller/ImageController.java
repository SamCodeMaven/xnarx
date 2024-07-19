package uz.xnarx.imageservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.xnarx.imageservice.constants.ProjectEndpoint;
import uz.xnarx.imageservice.dto.ImageDto;
import uz.xnarx.imageservice.entity.Image;
import uz.xnarx.imageservice.exception.ImageNotFoundException;
import uz.xnarx.imageservice.service.ImageService;


@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;


    @Operation(summary = "get image by image_name",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ImageDto.class)))))
    @GetMapping(value = ProjectEndpoint.IMAGE_NAME, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImageByName(@PathVariable String imageName) {
        ImageDto imageDto = imageService.getImageByName(imageName);
        return ResponseEntity.ok(imageDto.getData());

    }

    @Operation(summary = "get image by image_id",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ImageDto.class)))))
    @GetMapping(value = ProjectEndpoint.IMAGE_ID, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable Long id) {
        return imageService.getImage(id).getData();
    }


}