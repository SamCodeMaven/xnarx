package uz.xnarx.xnarx.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.xnarx.xnarx.entity.PriceHistory;
import uz.xnarx.xnarx.payload.PriceHistoryDto;

import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Integer> {

    @Query("SELECT NEW uz.xnarx.xnarx.payload.PriceHistoryDto(ph.id, ph.product_name, ph.store_name, ph.product_link, ph.price,ph.date) " +
            "FROM  PriceHistory AS ph WHERE LOWER( ph.product_name) LIKE LOWER(:product_name) ")
    Page<PriceHistoryDto> getAllProductsWithPriceHistory(@Param("product_name") String product_name, Pageable pageable);

    @Query("SELECT NEW uz.xnarx.xnarx.payload.PriceHistoryDto(ph.id, ph.product_name, ph.store_name, ph.product_link, ph.price, ph.date) " +
            "FROM PriceHistory AS ph WHERE ph.date=(SELECT MAX(ph2.date) FROM PriceHistory AS ph2 WHERE LOWER(ph2.product_name) = LOWER(:product_name)) " +
            "AND ph.price = (SELECT MIN(ph3.price) FROM PriceHistory ph3 WHERE ph3.product_name= ph.product_name AND ph3.date=ph.date) " +
            "AND LOWER(ph.product_name) LIKE LOWER(:product_name) " +
            "AND ph.price >= :min AND ph.price <= :max ")
    PriceHistoryDto searchByNameASC(@Param("product_name")String name, Double min, Double max);
}
