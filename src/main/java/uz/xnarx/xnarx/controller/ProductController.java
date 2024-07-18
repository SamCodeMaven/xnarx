package uz.xnarx.xnarx.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.xnarx.xnarx.constant.ProjectEndpoint;
import uz.xnarx.xnarx.payload.ProductDto;
import uz.xnarx.xnarx.payload.ProductResponse;
import uz.xnarx.xnarx.service.ProductService;
import uz.xnarx.xnarx.utils.ApplicationConstants;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "get all products with recent,cheapest price",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ProductDto.class)))))
    @GetMapping(value = ProjectEndpoint.PRODUCTS)
    public ResponseEntity<List<ProductDto>> getCheapestAllProduct() {
        List<ProductDto> products = productService.getAllProduct();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "get all products with recent,cheapest price sorting bu category",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ProductDto.class)))))
    @GetMapping(value = ProjectEndpoint.PRODUCT_CATEGORY)
    public ResponseEntity<ProductResponse> getMinMaxPriceProductByCategory(@PathVariable String categoryName,
                                                                           @RequestParam(value = "minPrice",
                                                        defaultValue = ApplicationConstants.DEFAULT_MIN_PRICE) Double minPrice,
                                                                           @RequestParam(value = "maxPrice",
                                                        defaultValue = ApplicationConstants.DEFAULT_MAX_PRICE) Double maxPrice,
                                                                           @RequestParam(value = "orderType",
                                                        defaultValue = ApplicationConstants.DEFAULT_ORDER_TYPE) Boolean orderType,

                                                                           @RequestParam(value = "page",
                                                        defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                                           @RequestParam(value = "size",
                                                        defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return ResponseEntity.ok(productService.getMinMaxProduct(categoryName, minPrice, maxPrice, orderType, page, size));
    }

    @Operation(summary = "get all products with recent,cheapest price sorting bu name",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ProductDto.class)))))
    @GetMapping(value = ProjectEndpoint.PRODUCT_NAME)
    public ResponseEntity<ProductResponse> getProductByName(@PathVariable(value = "productName") String productName,
                                          @RequestParam(value = "minPrice",
                                          defaultValue = ApplicationConstants.DEFAULT_MIN_PRICE) Double minPrice,
                                          @RequestParam(value = "maxPrice",
                                          defaultValue = ApplicationConstants.DEFAULT_MAX_PRICE) Double maxPrice,
                                          @RequestParam(value = "orderType",
                                          defaultValue = ApplicationConstants.DEFAULT_ORDER_TYPE) Boolean orderType,

                                          @RequestParam(value = "page",
                                                  defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER)Integer page,
                                          @RequestParam(value = "size",
                                                  defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE)Integer size
    ){
        return ResponseEntity.ok(productService.getProductByName(productName,minPrice,maxPrice,orderType,page,size));
    }
    @Operation(summary = "get product by Id",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ProductDto.class)))))
    @GetMapping(value = ProjectEndpoint.PRODUCT_ID)
    private ProductDto getProductById(@RequestParam(value = "productId") Integer productId) {
        return productService.findByProductId(productId);
    }
}
