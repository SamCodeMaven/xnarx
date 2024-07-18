package uz.xnarx.productservice.payload;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private String message;
    private boolean success;
    private Long totalElements;
    private Integer totalPages;
    private Object object;

    public ProductResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ProductResponse(String message, boolean success, Object object) {
        this.message = message;
        this.success = success;
        this.object = object;
    }
}
