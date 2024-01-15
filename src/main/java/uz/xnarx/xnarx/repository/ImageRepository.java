package uz.xnarx.xnarx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.xnarx.xnarx.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
