package ru.sibintek.vme.common.security;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.sibintek.vme.common.security.domain.Role;
import ru.sibintek.vme.common.security.domain.SecurityUser;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthenticationFacade {

    private static KeycloakSecurityContext getKeycloakSecurityContext(KeycloakAuthenticationToken authenticationToken) {
        KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) authenticationToken.getPrincipal();
        return principal.getKeycloakSecurityContext();
    }

    public SecurityUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && !(authentication instanceof AnonymousAuthenticationToken)) {
            final AccessToken token = getKeycloakSecurityContext((KeycloakAuthenticationToken) authentication).getToken();

            Long companyId = null;
            try {
                companyId = Long.valueOf((String) token.getOtherClaims().get("company_id"));
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            final Set<Role> roles = token.getRealmAccess().getRoles().stream()
                    .filter(Role::isPresent)
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
            return SecurityUser.of(token.getSubject(), token.getEmail(), token.getName(), companyId, roles);
        } else {
            return SecurityUser.anonymous();
        }
    }

    public boolean isAdmin() {
        return getCurrentUser().getRoles().contains(Role.ADMIN);
    }

    public boolean isSupport() {
        return getCurrentUser().getRoles().contains(Role.SUPPORT);
    }

    public boolean isClient() {
        return !isAdmin() && !isSupport() && getCurrentUser().getRoles().contains(Role.CLIENT);
    }

}
