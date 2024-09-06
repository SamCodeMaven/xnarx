package uz.xnarx.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.xnarx.productservice.entity.Characteristics;

public interface CharacteristicsRepository extends JpaRepository<Characteristics, Long> {
}
