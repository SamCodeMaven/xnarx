package uz.xnarx.productservice.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.xnarx.productservice.entity.Users;


import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Long> {


    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
            !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        Users userPrincipal = (Users) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }
}
