package uz.xnarx.xnarx.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.xnarx.xnarx.entity.PriceHistory;
import uz.xnarx.xnarx.payload.ProductResponse;
import uz.xnarx.xnarx.repository.PriceHistoryRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PriceHistoryService{
    private final PriceHistoryRepository priceHistoryRepository;

    public ProductResponse getPriceHistoryByProductId(Integer productId) {
        List<PriceHistory> priceHistory=priceHistoryRepository.findByProductId(productId);
        if (priceHistory.isEmpty()){
            return ProductResponse.builder().message("Price History not found").success(false).build();
        }
        return ProductResponse.builder().message("Success").success(true).object(priceHistory).build();
    }
}
