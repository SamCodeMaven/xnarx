package uz.xnarx.xnarx.payload;

import lombok.Data;

import java.util.Date;

@Data
public class PriceHistoryDto {
    private Integer id;
    private String product_name;
    private String store_name;
    private Integer price;
    private Integer productId;
    private Date date;
}
