package uz.xnarx.productservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.xnarx.productservice.entity.Characteristics;
import uz.xnarx.productservice.entity.PriceHistory;
import uz.xnarx.productservice.entity.Product;
import uz.xnarx.productservice.exception.NotFoundException;
import uz.xnarx.productservice.payload.CharacteristicsDto;
import uz.xnarx.productservice.payload.PriceHistoryDto;
import uz.xnarx.productservice.payload.ProductDto;
import uz.xnarx.productservice.payload.ProductResponse;
import uz.xnarx.productservice.payload.ProductWithHistoryDto;
import uz.xnarx.productservice.repository.CharacteristicsRepository;
import uz.xnarx.productservice.repository.PriceHistoryRepository;
import uz.xnarx.productservice.repository.ProductRepository;
import uz.xnarx.productservice.utils.CommonUtills;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static uz.xnarx.productservice.constant.ProjectConstants.DATE_RANGE;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    private final PriceHistoryRepository priceHistoryRepository;

    private final CharacteristicsRepository characteristicsRepository;


    @Transactional
    public List<ProductDto> getAllProduct() {
        return productRepository.getAllProduct();
    }

    @Transactional
    public ProductResponse getMinMaxProduct(String categoryName, Double minPrice, Double maxPrice, Boolean orderType, Integer page, Integer size) {
        Page<ProductDto> productPage = null;
        if (Boolean.TRUE.equals(orderType)) {
            productPage = productRepository.
                    getMinMaxProductASC(categoryName, minPrice, maxPrice, CommonUtills.simplePageable(page, size));
        }
        if (Boolean.FALSE.equals(orderType)) {
            productPage = productRepository.
                    getMinMaxProductDESC(categoryName, minPrice, maxPrice, CommonUtills.simplePageable(page, size));
        }
        return new ProductResponse("Success",
                true,
                Objects.requireNonNull(productPage).getTotalElements(),
                productPage.getTotalPages(),
                productPage.getContent());
    }

    @Transactional
    public ProductResponse getProductByName(String name, Double min, Double max, Boolean orderType, Integer page, Integer size) {
        Page<ProductDto> productPage;
        if (Boolean.TRUE.equals(orderType)) {
            productPage = productRepository
                    .searchByNameASC(name, min, max, CommonUtills.simplePageable(page, size));
        } else {
            productPage = productRepository.
                    searchByNameDESC(name, min, max, CommonUtills.simplePageable(page, size));
        }


        return new ProductResponse("Success",
                true,
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.getContent());
    }

    @Transactional
    public ProductWithHistoryDto findByProductId(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        LocalDate date = LocalDate.now().minusDays(DATE_RANGE);
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findByProductIdOrderByDateDesc(productId, date);

        ProductWithHistoryDto productWithHistoryDtos = objectMapper.convertValue(product, ProductWithHistoryDto.class);
        productWithHistoryDtos.setPriceHistory(priceHistoryList
                .stream().map(priceHistory -> objectMapper.convertValue(priceHistory, PriceHistoryDto.class)).toList());
        if (product.getGsmarena() != null) {
            Optional<Characteristics> characteristics = characteristicsRepository.findById(product.getGsmarena().getId());
            if (characteristics.isPresent()) {
                productWithHistoryDtos.setCharacteristics(objectMapper.convertValue(characteristics.get(), CharacteristicsDto.class));
                return productWithHistoryDtos;
            }
        }

        return productWithHistoryDtos;
    }

    public void updateIsActiveForAllProducts() {
        List<Product> productsWithNullIsActive = productRepository.findAll();
        productsWithNullIsActive.forEach(product -> product.setIsActive(true));
        productRepository.saveAll(productsWithNullIsActive);
    }
}
