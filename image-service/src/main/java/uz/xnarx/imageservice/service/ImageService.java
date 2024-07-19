package uz.xnarx.imageservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.xnarx.imageservice.dto.ImageDto;
import uz.xnarx.imageservice.entity.Image;
import uz.xnarx.imageservice.exception.ImageNotFoundException;
import uz.xnarx.imageservice.repositary.ImageRepository;

import java.io.IOException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ObjectMapper objectMapper;
    //

    @Transactional
    public ImageDto getImageByName(String name) {
        Optional<Image> image = Optional.ofNullable(imageRepository.findByName(name));
        ImageDto imageDto;
        if (image.isPresent()) {
            imageDto = objectMapper.convertValue(image.get(), ImageDto.class);
            return imageDto;
        }
        return null;
    }

    @Transactional
    public Image getImage(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found!"));
    }
}