package uz.xnarx.productservice.constant;

import lombok.experimental.UtilityClass;


@UtilityClass
public class ProjectEndpoint {
    public static final String USER_AUTH="/api/auth/authenticate";
    public static final String USER_TOKEN="/api/auth/refresh-token";
    public static final String USER_REGISTER="/api/user/register";
    public static final String USERS="/api/user/getAll";
    public static final String USER_ID="/api/user/getById/{id}";
    public static final String USER_INFO="/api/user/info";
    public static final String USER_ENABLE="/api/user/enable/{userId}";
    public static final String USER_DISABLE="/api/user/disable/{userId}";
    public static final String PRODUCTS = "api/product/getAll";
    public static final String PRODUCT_CATEGORY = "api/product/category/{categoryName}";
    public static final String PRODUCT_NAME = "api/product/search";
    public static final String PRODUCT_ID = "api/product/search/{id}";
    public static final String PRICE_HISTORY = "api/priceHistory/search/{productId}";
}
