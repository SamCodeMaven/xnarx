package uz.xnarx.productservice.constant;

import lombok.experimental.UtilityClass;


@UtilityClass
public class ProjectEndpoint {
    public static final String PRODUCTS = "api/product/getAll";
    public static final String PRODUCT_CATEGORY = "api/product/category/{categoryName}";
    public static final String PRODUCT_NAME = "api/product/search/{productName}";
    public static final String PRODUCT_ID = "api/product/search";
    public static final String PRICE_HISTORY = "api/priceHistory/search/{productId}";
}
