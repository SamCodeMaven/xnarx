package uz.xnarx.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.xnarx.productservice.entity.Users;
import uz.xnarx.productservice.entity.VerificationToken;
import uz.xnarx.productservice.exception.CustomException;
import uz.xnarx.productservice.payload.enums.ErrorCode;
import uz.xnarx.productservice.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static uz.xnarx.productservice.constant.ProjectConstants.VERIFICATION_TOKEN_EXPIRES_IN_MINUTES;

/**
 * @author Samandar Daminov
 * Date: 10/24/2024
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserService userService;

    public void saveVerificationToken(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token).orElseThrow();
    }


    public String saveAndGetVerificationToken(Users user) {
        VerificationToken verificationToken=saveVerificationToken(user);
        return verificationToken.getToken();
    }
    public VerificationToken saveVerificationToken(Users user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .verifiedAt(LocalDateTime.now().plusMinutes(VERIFICATION_TOKEN_EXPIRES_IN_MINUTES))
                .user(user)
                .build();

        return verificationTokenRepository.save(verificationToken);

    }

    public String verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_TOKEN_NOT_FOUND));
        isTokenVerifiedOrExpired(verificationToken);
        verificationToken.setVerifiedAt(LocalDateTime.now());
        userService.enableUser(verificationToken.getUser().getId());
        return "Confirmed";


    }

    private void isTokenVerifiedOrExpired(VerificationToken verificationToken) {
        isTokenNotVerified(verificationToken);
        isTokenNotExpired(verificationToken);
    }

    private void isTokenNotVerified(VerificationToken verificationToken) {
        if (verificationToken.getVerifiedAt()!=null){
            throw new CustomException(ErrorCode.TOKEN_ALREADY_CONFIRMED);
        }
    }
    private void isTokenNotExpired(VerificationToken verificationToken) {
        LocalDateTime expiresAt = verificationToken.getVerifiedAt();
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.TOKEN_ALREADY_EXPIRED);
        }
    }

}
