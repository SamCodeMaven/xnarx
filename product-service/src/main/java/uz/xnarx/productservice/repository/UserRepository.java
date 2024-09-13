package uz.xnarx.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.xnarx.productservice.entity.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {


    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhone(String phone);

}
