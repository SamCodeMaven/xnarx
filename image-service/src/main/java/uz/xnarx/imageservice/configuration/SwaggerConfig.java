package uz.xnarx.imageservice.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(info());
    }

    private Info info() {
        return new Info()
                .title("Image")
                .description("Image service for Smart Bank")
                .version("V1.0")
                .contact(contact());
    }

    private Contact contact() {
        return new Contact()
                .name("Samandar")
                .email("samandardaminov1295@gmail.com");
    }
}
