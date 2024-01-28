package uz.xnarx.xnarx.payload;

import lombok.Data;

import java.util.Date;

@Data
public class PriceHistoryDto {
    private Integer id;
    private String product_name;
    private String product_link;
    private String store_name;
    private Double price;
    private Date date;

    public PriceHistoryDto(Integer id, String product_name, String product_link, String store_name, Double price, Date date) {
        this.id = id;
        this.product_name = product_name;
        this.product_link = product_link;
        this.store_name = store_name;
        this.price = price;
        this.date = date;
    }
}
