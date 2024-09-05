package uz.xnarx.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.xnarx.productservice.entity.Users;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {


    Optional<Users> findByEmail(String email);

    Users findByPhone(String phone);

}
