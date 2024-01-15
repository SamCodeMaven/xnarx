package uz.xnarx.xnarx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.xnarx.xnarx.entity.Image;
import uz.xnarx.xnarx.repository.ImageRepository;

import java.io.IOException;

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
    public Image getImage(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found!"));
    }
}