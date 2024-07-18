package uz.xnarx.xnarx.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.xnarx.xnarx.entity.Product;
import uz.xnarx.xnarx.exception.NotFoundException;
import uz.xnarx.xnarx.payload.ProductResponse;
import uz.xnarx.xnarx.payload.ProductDto;
import uz.xnarx.xnarx.repository.PriceHistoryRepository;
import uz.xnarx.xnarx.repository.ProductRepository;
import uz.xnarx.xnarx.utils.CommonUtills;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

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
        Page<ProductDto> productPage=null;
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

    public ProductDto findByProductId(Integer productId) {
        Product product=productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        return new ProductDto(product.getId(),product.getProductImage(),product.getProductName(),product.getCategoryName());
    }
}
