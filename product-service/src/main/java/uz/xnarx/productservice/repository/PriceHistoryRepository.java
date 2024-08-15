package uz.xnarx.productservice.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.xnarx.productservice.entity.PriceHistory;

import java.time.LocalDate;
import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Integer> {

    List<PriceHistory> findByProductId(Integer productId);

    @Query("SELECT ph FROM PriceHistory AS ph WHERE ph.product.id=:productId AND DATE(ph.date)>=:date order by ph.date DESC")
    List<PriceHistory> findByProductIdOrderByDateDesc(@Param("productId") Integer productId, LocalDate date);

    @Modifying
    @Transactional
    @Query("DELETE FROM PriceHistory ph WHERE ph.product.id IN (SELECT p.id FROM Product p WHERE p.categoryName <> 'Smartfonlar')")
    void deleteAssociatedPriceHistories();
}
