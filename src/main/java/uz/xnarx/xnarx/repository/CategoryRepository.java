package uz.xnarx.xnarx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.xnarx.xnarx.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
