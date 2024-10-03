package uz.xnarx.productservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.xnarx.productservice.entity.Product;
import uz.xnarx.productservice.payload.ProductDto;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByIsActiveIsNull();

    @Query("SELECT NEW uz.xnarx.productservice.payload.ProductDto(p.id, p.productName, p.productImage, p.categoryName, ph.date, MIN(ph.price), ph.storeName, ph.productLink, p.isActive) " +
            "FROM Product p " +
            "JOIN p.priceHistory ph " +
            "WHERE DATE(ph.date) = (SELECT MAX(DATE(ph2.date)) FROM PriceHistory ph2 WHERE ph2.product = p) " +
            "AND ph.price = (SELECT MIN(ph3.price) FROM PriceHistory ph3 WHERE ph3.product = p AND DATE(ph3.date) = DATE(ph.date)) " +
            "AND p.isActive=true "+
            "GROUP BY p.id, p.productName, p.productImage, p.categoryName, ph.date, ph.storeName, ph.productLink " +
            "ORDER BY p.productName")
    List<ProductDto> getAllProduct();

    @Query("SELECT NEW uz.xnarx.productservice.payload.ProductDto(p.id, p.productName, p.productImage, p.categoryName, ph.date, MIN(ph.price), ph.storeName, ph.productLink,p.isActive) " +
            "FROM Product p JOIN p.priceHistory ph " +
            "WHERE ph.date = (SELECT MAX(DATE(ph2.date)) FROM PriceHistory ph2 WHERE ph2.product = p) " +
            "AND ph.price = (SELECT MIN(ph3.price) FROM PriceHistory ph3 WHERE ph3.product= p AND DATE(ph3.date)=DATE(ph.date)) " +
            "AND LOWER(p.categoryName) LIKE Lower(:category_name)" +
            "AND ph.price>:minPrice AND ph.price<:maxPrice AND p.isActive=true " +
            "GROUP BY p.id, p.productName, p.productImage, p.categoryName, ph.date, ph.storeName, ph.productLink " +
            "ORDER BY MIN(ph.price) ASC ")
    Page<ProductDto> getMinMaxProductASC(@Param("category_name") String category_name, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);

    @Query("SELECT NEW uz.xnarx.productservice.payload.ProductDto(p.id, p.productName, p.productImage, p.categoryName, ph.date, MIN(ph.price), ph.storeName, ph.productLink, p.isActive) " +
            "FROM Product p JOIN p.priceHistory ph " +
            "WHERE ph.date = (SELECT MAX(DATE(ph2.date)) FROM PriceHistory ph2 WHERE ph2.product = p) " +
            "AND ph.price = (SELECT MIN(ph3.price) FROM PriceHistory ph3 WHERE ph3.product= p AND DATE(ph3.date)=DATE(ph.date)) " +
            "AND LOWER(p.categoryName) LIKE Lower(:category_name)" +
            "AND ph.price>:minPrice AND ph.price<:maxPrice AND p.isActive=true " +
            "GROUP BY p.id, p.productName, p.productImage, p.categoryName, ph.date, ph.storeName, ph.productLink " +
            "ORDER BY MIN(ph.price) DESC ")
    Page<ProductDto> getMinMaxProductDESC(@Param("category_name") String category_name, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);

    @Query("SELECT NEW uz.xnarx.productservice.payload.ProductDto(p.id, p.productName, p.productImage, p.categoryName, ph.date, MIN(ph.price), ph.storeName, ph.productLink, p.isActive) " +
            "FROM Product p JOIN p.priceHistory ph " +
            "WHERE ph.date = (SELECT MAX(DATE(ph2.date)) FROM PriceHistory ph2 WHERE ph2.product = p) " +
            "AND ph.price = (SELECT MIN(ph3.price) FROM PriceHistory ph3 WHERE ph3.product= p AND DATE(ph3.date)=DATE(ph.date)) " +
            "AND LOWER(p.productName) LIKE Lower(CONCAT('%',:product_name,'%'))" +
            "AND ph.price>:minPrice AND ph.price<:maxPrice AND p.isActive=true " +
            "GROUP BY p.id, p.productName, p.productImage, p.categoryName, ph.date, ph.storeName, ph.productLink " +
            "ORDER BY MIN(ph.price) ASC ")
    Page<ProductDto> searchByNameASC(String product_name, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);

    @Query("SELECT NEW uz.xnarx.productservice.payload.ProductDto(p.id, p.productName, p.productImage, p.categoryName, ph.date, MIN(ph.price), ph.storeName, ph.productLink, p.isActive) " +
            "FROM Product p JOIN p.priceHistory ph " +
            "WHERE ph.date = (SELECT MAX(ph2.date) FROM PriceHistory ph2 WHERE ph2.product = p) " +
            "AND ph.price = (SELECT MIN(ph3.price) FROM PriceHistory ph3 WHERE ph3.product= p AND ph3.date=ph.date) " +
            "AND LOWER(p.productName) LIKE Lower(CONCAT('%',:product_name,'%'))" +
            "AND ph.price>:minPrice AND ph.price<:maxPrice AND p.isActive=true " +
            "GROUP BY p.id, p.productName, p.productImage, p.categoryName, ph.date, ph.storeName, ph.productLink  " +
            "ORDER BY MIN(ph.price) DESC ")
    Page<ProductDto> searchByNameDESC(String product_name, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);
    //    @Query("SELECT NEW uz.xnarx.xnarx.payload.ProductDto(p.id, p.product_name, p.store_name, p.product_link,p.product_image, p.characteristics, p.category_name,  MIN(ph.price),MAX(ph.date)) " +
//            "FROM Product p JOIN p.priceHistory ph " +
//            "WHERE ph.date = (SELECT MAX(ph2.date) FROM PriceHistory ph2 WHERE ph2.product = p) " +
//            "AND ph.price = (SELECT MIN(ph3.price) FROM PriceHistory ph3 WHERE ph3.product_name= ph.product_name AND ph3.date=ph.date) " +
//            "AND LOWER(p.category_name) LIKE Lower(:category_name)" +
//            "GROUP BY p.product_name, p.id, p.product_link, p.characteristics, p.category_name, p.store_name, ph.date " +
//            "ORDER BY p.product_name, MIN(ph.price)")
//    Page<ProductDto> findProductsByCategory_name(String category_name, Pageable pageable);
//


//
//
//    @Query("SELECT NEW uz.xnarx.xnarx.payload.ProductDto(p.id, p.product_name, p.product_link, p.product_image, p.characteristics, p.category_name, p.store_name, MIN(ph.price), MAX(ph.date)) " +
//            "FROM Product p JOIN p.priceHistory ph " +
//            "WHERE ph.date = (SELECT MAX(ph2.date) FROM PriceHistory ph2 WHERE ph2.product.id = p.id) " +
//            "AND p.id = :id " +
//            "GROUP BY p.id, p.product_name, p.product_link, p.characteristics, p.category_name, p.store_name, ph.date")
//    ProductDto findCheapestById(Integer id);
//
//    @Query("SELECT p FROM Product p WHERE LOWER(p.product_name) = LOWER(:product_name)")
//    Page<Product> findAllProductsWithHistory(@Param("product_name") String product_name, Pageable pageable);
//

}
