package uz.xnarx.xnarx.payload;

import lombok.Data;
import uz.xnarx.xnarx.entity.Brand;
import uz.xnarx.xnarx.entity.Category;
import uz.xnarx.xnarx.entity.Store;

import java.util.List;

@Data
public class HomePageResponse {
    private List<Category> categories;
    private List<Store> stores;
    private List<Brand> brands;

    public HomePageResponse(List<Category> categories, List<Store> stores, List<Brand> brands) {
        this.categories = categories;
        this.stores = stores;
        this.brands = brands;
    }
}
