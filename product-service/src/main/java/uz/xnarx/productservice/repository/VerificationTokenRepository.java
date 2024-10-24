package uz.xnarx.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.xnarx.productservice.entity.VerificationToken;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Samandar Daminov
 * Date: 10/24/2024
 */

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

    Optional<VerificationToken> findByToken(String token);

}
