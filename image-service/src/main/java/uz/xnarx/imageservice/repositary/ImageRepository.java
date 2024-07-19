package uz.xnarx.imageservice.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.xnarx.imageservice.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image as i where i.name=:name")
    Image findByName(String name);
}
