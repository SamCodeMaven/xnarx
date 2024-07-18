package uz.xnarx.xnarx.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.xnarx.xnarx.constant.ProjectEndpoint;
import uz.xnarx.xnarx.payload.PriceHistoryDto;
import uz.xnarx.xnarx.payload.ProductDto;
import uz.xnarx.xnarx.payload.ProductResponse;
import uz.xnarx.xnarx.service.PriceHistoryService;
import uz.xnarx.xnarx.utils.ApplicationConstants;


@RestController
@RequiredArgsConstructor
public class PriceHistoryController {


    private final PriceHistoryService priceHistoryService;

    @Operation(summary = "get all price history by product_id",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = PriceHistoryDto.class)))))
    @GetMapping(value = ProjectEndpoint.PRICE_HISTORY)
    public ResponseEntity<ProductResponse> getPriceHistoryByProductId(@PathVariable Integer productId) {
        return ResponseEntity.ok(priceHistoryService.getPriceHistoryByProductId(productId));
    }
}
