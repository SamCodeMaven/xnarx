package uz.xnarx.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.xnarx.productservice.constant.ProjectEndpoint;
import uz.xnarx.productservice.service.VerificationTokenService;

/**
 * @author Samandar Daminov
 * Date: 10/24/2024
 */

@RestController
@RequestMapping(ProjectEndpoint.VERIFICATION_TOKEN)
@RequiredArgsConstructor
public class VerificationTokenController {

    private final VerificationTokenService verificationTokenService;

    @GetMapping(ProjectEndpoint.VERIFY_TOKEN)
    public String verifyToken(@RequestParam(value = "token") String token) {
        return verificationTokenService.verifyToken(token);
    }
}
