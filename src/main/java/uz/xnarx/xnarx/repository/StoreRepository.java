package uz.xnarx.xnarx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.xnarx.xnarx.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Integer> {
}
