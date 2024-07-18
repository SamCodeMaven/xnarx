package uz.xnarx.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.xnarx.productservice.entity.PriceHistory;

import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Integer> {

    List<PriceHistory> findByProductId(Integer productId);
}
