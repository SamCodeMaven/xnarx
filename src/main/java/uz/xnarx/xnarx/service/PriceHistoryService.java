package uz.xnarx.xnarx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.xnarx.xnarx.entity.PriceHistory;
import uz.xnarx.xnarx.entity.Product;
import uz.xnarx.xnarx.payload.ApiResponse;
import uz.xnarx.xnarx.payload.PriceHistoryDto;
import uz.xnarx.xnarx.repository.PriceHistoryRepository;
import uz.xnarx.xnarx.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PriceHistoryService {
    @Autowired
    PriceHistoryRepository priceHistoryRepository;

    @Autowired
    ProductRepository productRepository;

    public ApiResponse savePriceHistory(PriceHistoryDto dto, Integer productId){
        try {
            // Find the existing PriceHistory or create a new one
            PriceHistory priceHistory = (dto.getId() != null) ?
                    priceHistoryRepository.findById(dto.getId())
                            .orElseThrow(() -> new IllegalStateException("PriceHistory with this id not found")) :
                    new PriceHistory();

            // Find the product using the provided productId
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalStateException("Product not found"));

            // Set properties from the found product and DTO
            priceHistory.setProduct(product);  // Assuming you have a setProduct method in PriceHistory
            priceHistory.setPrice(dto.getPrice());
            priceHistory.setDate(dto.getDate());

            // Save the priceHistory
            priceHistoryRepository.save(priceHistory);

            // Return response
            return new ApiResponse(dto.getId() != null ? "Edited" : "Saved", true);
        } catch (Exception e) {
            // Log the error for debugging
            e.printStackTrace(); // Consider using a logger in a real application
            return new ApiResponse("Error: " + e.getMessage(), false);
        }
    }

}
