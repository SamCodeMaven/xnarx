package uz.xnarx.xnarx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.xnarx.xnarx.entity.Brand;
import uz.xnarx.xnarx.entity.Category;
import uz.xnarx.xnarx.entity.Store;
import uz.xnarx.xnarx.payload.HomePageResponse;
import uz.xnarx.xnarx.repository.BrandRepository;
import uz.xnarx.xnarx.repository.CategoryRepository;
import uz.xnarx.xnarx.repository.StoreRepository;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private BrandRepository brandRepository;

    public HomeController() {
    }

    @GetMapping("/home")
    public HttpEntity<?> getHomePageData() {
        List<Category> categories = categoryRepository.findAll();
        List<Store> stores = storeRepository.findAll();
        List<Brand> brands = brandRepository.findAll();

        HomePageResponse homePageResponse = new HomePageResponse(categories, stores, brands);

        return ResponseEntity.ok(homePageResponse);
    }
}
