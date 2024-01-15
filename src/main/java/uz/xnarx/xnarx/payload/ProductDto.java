package uz.xnarx.xnarx.payload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class ProductDto {
    private Integer id;

    private String name;

    private String store_name;

    private String product_link;

    private String characteristics;

    private String category_name;

    private Integer price;

    private Date date;

    public ProductDto() {
        this.id = id;
        this.name = name;
        this.store_name = store_name;
        this.product_link = product_link;
        this.characteristics = characteristics;
        this.category_name = category_name;
    }




    public ProductDto(Integer id, String name, String store_name, String product_link, String characteristics, String category_name, Integer price, Date date) {
        this.id = id;
        this.name = name;
        this.store_name = store_name;
        this.product_link = product_link;
        this.characteristics = characteristics;
        this.category_name = category_name;
        this.price = price;
        this.date = date;
    }


}
