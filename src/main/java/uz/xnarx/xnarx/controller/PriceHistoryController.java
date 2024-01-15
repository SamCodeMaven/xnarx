package uz.xnarx.xnarx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.xnarx.xnarx.payload.ApiResponse;
import uz.xnarx.xnarx.payload.PriceHistoryDto;
import uz.xnarx.xnarx.service.PriceHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/priceHistory")
public class PriceHistoryController {

    @Autowired
    PriceHistoryService priceHistoryService;

    @PostMapping("/save/{productId}")
    public HttpEntity<?> savePriceHistory(@RequestBody PriceHistoryDto priceHistoryDto, Integer productId) {
        ApiResponse apiResponse = priceHistoryService.savePriceHistory(priceHistoryDto, productId);
        return ResponseEntity
                .status(apiResponse.isSuccess()?apiResponse.getMessage().equals("Saved")?201:202:409)
                .body(apiResponse);
    }

}
