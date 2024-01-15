package uz.xnarx.xnarx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.xnarx.xnarx.exception.ResourceNotFoundException;
import uz.xnarx.xnarx.payload.ApiResponse;
import uz.xnarx.xnarx.payload.ProductDto;
import uz.xnarx.xnarx.service.ProductService;
import uz.xnarx.xnarx.utils.ApplicationConstants;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/save")
    public HttpEntity<?> saveProduct(@RequestBody ProductDto productDto){
        ApiResponse apiResponse = productService.saveProduct(productDto);
        return ResponseEntity
                .status(apiResponse.isSuccess()?apiResponse.getMessage().equals("Saved")?201:202:409)
                .body(apiResponse);
    }

    @GetMapping("/")
    public HttpEntity<?> getCheapestAllProduct() {
        List<ProductDto> products = productService.getAllProduct();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{categoryName}")
    public HttpEntity<?> getMinMaxPriceProduct0(@PathVariable String categoryName,
                                                @RequestParam("minPrice") Integer minPrice,
                                                @RequestParam("maxPrice") Integer maxPrice,
                                                @RequestParam(value = "page",
                                                        defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER)Integer page,
                                                @RequestParam(value = "size",
                                                        defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE)Integer size){
        return ResponseEntity.ok(productService.getMinMaxProduct(categoryName,minPrice,maxPrice,page,size));

    }

    @GetMapping("/getByName/{product_name}")
    public HttpEntity<?> getProductByName(@PathVariable(value = "product_name") String name,
                                          @RequestParam(value = "page",
                                                  defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER)Integer page,
                                          @RequestParam(value = "size",
                                                  defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE)Integer size
    ){
        return ResponseEntity.ok(productService.getProductByName(name,page,size));
    }

    @GetMapping("/getAllPH/{product_name}")
        public HttpEntity<?> getAllProductHistory(@PathVariable(value = "product_name") String name,
                @RequestParam(value = "page",
                        defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER)Integer page,
                @RequestParam(value = "size",
                        defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE)Integer size
    ){
            return ResponseEntity.ok(productService.getAllProductHistory(name,page,size));
    }

    @GetMapping("/getByCategory/{categoryName}")
    public HttpEntity<?> getProductsByCategoryName(@PathVariable String categoryName,@RequestParam(value = "page",
            defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER)Integer page,
                                                                 @RequestParam(value = "size",
                                                                         defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE)Integer size
    ) {
        return ResponseEntity.ok(productService.getProductByCategory(categoryName,page,size));
    }

    @GetMapping("/getById/{id}")
    public HttpEntity<?> getProductById(@PathVariable Integer id) {
        ApiResponse response = productService.getProductById(id);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }
}
