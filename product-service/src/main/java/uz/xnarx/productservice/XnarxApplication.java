package uz.xnarx.productservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import uz.xnarx.productservice.entity.enumurated.Role;
import uz.xnarx.productservice.payload.UserDto;
import uz.xnarx.productservice.service.UserService;

@SpringBootApplication
public class XnarxApplication {

    public static void main(String[] args) {
        SpringApplication.run(XnarxApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            UserService service
    ) {
        return args -> {
            var admin = UserDto.builder()
                    .firstName("Admin")
                    .lastName("Admin1")
                    .email("admin@mail.com")
                    .password("password")
                    .phone("+998901001000")
                    .address("Tashkent")
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            System.out.println("Admin token: " + service.registerUser(admin).getAccessToken());
            System.out.println(admin.toString());
        };
    }

}
