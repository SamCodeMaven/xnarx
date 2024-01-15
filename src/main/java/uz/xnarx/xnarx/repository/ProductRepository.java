package uz.xnarx.xnarx.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.xnarx.xnarx.entity.Product;
import uz.xnarx.xnarx.payload.ProductDto;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT NEW uz.xnarx.xnarx.payload.ProductDto(p.id, p.product_name, p.product_link, p.characteristics, p.category_name, p.store_name, MIN(ph.price),MAX(ph.date)) " +
            "FROM Product p JOIN p.priceHistory ph " +
            "WHERE ph.date = (SELECT MAX(ph2.date) FROM PriceHistory ph2 WHERE ph2.product = p) " +
            "AND ph.price = (SELECT MIN(ph3.price) FROM PriceHistory ph3 WHERE ph3.product_name= ph.product_name AND ph3.date=ph.date) " +
            "AND LOWER(p.category_name) LIKE Lower(:category_name)" +
            "GROUP BY p.product_name, p.id, p.product_link, p.characteristics, p.category_name, p.store_name " +
            "ORDER BY p.product_name, MIN(ph.price)")
    Page<ProductDto> findProductsByCategory_name(String category_name, Pageable pageable);


    @Query("SELECT NEW uz.xnarx.xnarx.payload.ProductDto(p.id, p.product_name, p.product_link, p.characteristics, p.category_name, p.store_name, MIN(ph.price), MAX(ph.date), p.product_image) " +
            "FROM Product p JOIN p.priceHistory ph " +
            "WHERE ph.date = (SELECT MAX(ph2.date) FROM PriceHistory ph2 WHERE ph2.product = p) " +
            "AND ph.price = (SELECT MIN(ph3.price) FROM PriceHistory ph3 WHERE ph3.product_name= ph.product_name AND ph3.date=ph.date) " +
            "AND LOWER(p.product_name) LIKE Lower(CONCAT('%',:product_name,'%'))" +
            "GROUP BY p.product_name, p.id, p.product_link, p.characteristics, p.category_name, p.store_name , ph.date " +
            "ORDER BY p.product_name, MIN(ph.price)")
    Page<ProductDto> searchByName(String product_name,Pageable pageable);

    @Query("SELECT NEW uz.xnarx.xnarx.payload.ProductDto(p.id, p.product_name, p.product_link, p.characteristics, p.category_name, p.store_name, MIN(ph.price), MAX(ph.date)) " +
            "FROM Product p JOIN p.priceHistory ph " +
            "WHERE ph.date = (SELECT MAX(ph2.date) FROM PriceHistory ph2 WHERE ph2.product = p) " +
            "AND ph.price = (SELECT MIN(ph3.price) FROM PriceHistory ph3 WHERE ph3.product_name= ph.product_name AND ph3.date=ph.date) " +
            "GROUP BY p.product_name, p.id, p.product_link, p.characteristics, p.category_name, p.store_name, ph.date " +
            "ORDER BY p.product_name, MIN(ph.price)")
    List<ProductDto> findCheapestSmartphonesByProductName();

    @Query("SELECT NEW uz.xnarx.xnarx.payload.ProductDto(p.id, p.product_name, p.product_link, p.characteristics, p.category_name, p.store_name, MIN(ph.price), MAX(ph.date)) " +
            "FROM Product p JOIN p.priceHistory ph " +
            "WHERE ph.date = (SELECT MAX(ph2.date) FROM PriceHistory ph2 WHERE ph2.product.id = p.id) " +
            "AND p.id = :id " +
            "GROUP BY p.id, p.product_name, p.product_link, p.characteristics, p.category_name, p.store_name, ph.date")
    ProductDto findCheapestById(Integer id);

    @Query("SELECT p FROM Product p WHERE LOWER(p.product_name) = LOWER(:product_name)")
    Page<Product> findAllProductsWithHistory(@Param("product_name") String product_name, Pageable pageable);
}
