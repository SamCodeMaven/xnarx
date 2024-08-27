package uz.xnarx.productservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.xnarx.productservice.entity.PriceHistory;
import uz.xnarx.productservice.entity.Product;
import uz.xnarx.productservice.exception.NotFoundException;
import uz.xnarx.productservice.payload.PriceHistoryDto;
import uz.xnarx.productservice.payload.ProductResponse;
import uz.xnarx.productservice.payload.ProductDto;
import uz.xnarx.productservice.payload.ProductWithHistoryDto;
import uz.xnarx.productservice.repository.PriceHistoryRepository;
import uz.xnarx.productservice.repository.ProductRepository;
import uz.xnarx.productservice.utils.CommonUtills;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    private final PriceHistoryRepository priceHistoryRepository;


    @Transactional
    public List<ProductDto> getAllProduct() {
        return productRepository.getAllProduct();
    }

    @Transactional
    public ProductResponse getMinMaxProduct(String categoryName, Double minPrice, Double maxPrice, Boolean orderType, Integer page, Integer size) {
        Page<ProductDto> productPage=null;
        if (orderType) {
            productPage = productRepository.
                    getMinMaxProductASC(categoryName, minPrice, maxPrice, CommonUtills.simplePageable(page, size));
        }
        if(!orderType){
            productPage = productRepository.
                    getMinMaxProductDESC(categoryName, minPrice, maxPrice, CommonUtills.simplePageable(page, size));
        }
        return new ProductResponse("Success",
                true,
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.getContent());
    }

    @Transactional
    public ProductResponse getProductByName(String name,Double min,Double max, Boolean orderType, Integer page, Integer size) {
        Page<ProductDto> productPage;
        if (orderType) {
            productPage = productRepository
                    .searchByNameASC(name, min,max, CommonUtills.simplePageable(page, size));
        }else {
            productPage=productRepository.
                    searchByNameDESC(name, min,max, CommonUtills.simplePageable(page, size));
        }


        return new ProductResponse("Success",
                true,
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.getContent());
    }

    @Transactional
    public ProductWithHistoryDto findByProductId(Integer productId) {
        Product product=productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        LocalDate date=LocalDate.now().minusDays(100);
        List<PriceHistory> priceHistoryList=priceHistoryRepository.findByProductIdOrderByDateDesc(productId,date);

        ProductWithHistoryDto productWithHistoryDtos=objectMapper.convertValue(product, ProductWithHistoryDto.class);
        productWithHistoryDtos.setPriceHistory(priceHistoryList
                .stream().map(priceHistory -> objectMapper.convertValue(priceHistory, PriceHistoryDto.class)).toList());

        return productWithHistoryDtos;
    }
}
