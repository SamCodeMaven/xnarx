package uz.xnarx.productservice.service.component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import uz.xnarx.productservice.repository.PriceHistoryRepository;
import uz.xnarx.productservice.repository.ProductRepository;

@Component
@RequiredArgsConstructor
public class ProductComponent {

    private final ProductRepository productRepository;
    private final PriceHistoryRepository priceHistoryRepository;
//
//    @PostConstruct
//    public void deleteAllExceptSmartfonlar(){
//        priceHistoryRepository.deleteAssociatedPriceHistories();
//        productRepository.deleteAllExceptSmartPhone();
//    }
}
