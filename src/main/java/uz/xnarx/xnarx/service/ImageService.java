package uz.xnarx.xnarx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.xnarx.xnarx.entity.Image;
import uz.xnarx.xnarx.exception.ImageNotFoundException;
import uz.xnarx.xnarx.repository.ImageRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
@Transactional
@Service
public class ImageService {

    @Autowired
    private  ImageRepository imageRepository;

    public Image store(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setData(file.getBytes());
        return imageRepository.save(image);
    }
//
    public Image getImageByName(String name) {
        Optional<Image> image = Optional.ofNullable(imageRepository.findByName(name));
        return image.orElseThrow(() -> new ImageNotFoundException("Image with name '" + name + "' not found"));
    }


    public Image getImage(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found!"));
    }
}