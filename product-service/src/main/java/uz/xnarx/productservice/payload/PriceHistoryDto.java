package uz.xnarx.productservice.payload;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceHistoryDto {
    private Integer id;
    private String storeName;
    private String productLink;
    private Double price;
    private Timestamp date;
    private Integer product;

}
