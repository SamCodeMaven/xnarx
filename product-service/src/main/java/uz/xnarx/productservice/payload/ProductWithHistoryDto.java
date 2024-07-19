package uz.xnarx.productservice.payload;


import lombok.*;
import uz.xnarx.productservice.entity.PriceHistory;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithHistoryDto {

    private Integer id;

    private String productName;

    private String productImage;

    private String categoryName;

    private List<PriceHistoryDto> priceHistory;


}
