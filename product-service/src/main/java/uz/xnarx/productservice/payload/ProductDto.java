package uz.xnarx.productservice.payload;

import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Integer id;

    private String name;

    private String product_image;

    private String category_name;

    private Timestamp date;

    private Double price;

    private String store_name;

    private String product_link;

    public ProductDto(Integer id, String name, String product_image, String category_name) {
        this.id = id;
        this.name = name;
        this.product_image = product_image;
        this.category_name = category_name;
    }
}
