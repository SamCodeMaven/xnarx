package uz.xnarx.xnarx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.xnarx.xnarx.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT new uz.xnarx.xnarx.entity.Image(i.id, i.name, i.data) FROM Image as i where i.name=:name")
    Image findByName(String name);
}
