package uz.xnarx.xnarx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.xnarx.xnarx.entity.Image;
import uz.xnarx.xnarx.exception.ImageNotFoundException;
import uz.xnarx.xnarx.service.ImageService;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/saveImage")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            Image savedImage = imageService.store(file);
            return ResponseEntity.ok(savedImage.getName());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Could not upload the file: " + file.getOriginalFilename());
        }
    }

    @GetMapping(value = "/name/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImageByName(@PathVariable String imageName) {
        try {
            Image image = imageService.getImageByName(imageName);
            return ResponseEntity.ok(image.getData());
        } catch (ImageNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Image not found: " + imageName);
        }
    }
    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable Long id) {
        return imageService.getImage(id).getData();
    }


}