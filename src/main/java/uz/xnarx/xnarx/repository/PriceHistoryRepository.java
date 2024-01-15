package uz.xnarx.xnarx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.xnarx.xnarx.entity.PriceHistory;
import uz.xnarx.xnarx.payload.PriceHistoryDto;

import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Integer> {
    @Query(value = "select * from student where name like :ism",nativeQuery = true)
    List<PriceHistory> searchByName(@Param(value = "ism")String name);

}
