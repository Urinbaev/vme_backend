package ru.sibintek.vme.common;

import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sibintek.vme.common.security.AuthenticationFacade;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final AuthenticationFacade authenticationFacade;

    @PreAuthorize("hasAnyRole('CLIENT')")
    @GetMapping("/api/test")
    public String test(Principal principal) {

        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) principal;
        KeycloakPrincipal<?> kcPrincipal =(KeycloakPrincipal<?>)token.getPrincipal();
        return kcPrincipal.getKeycloakSecurityContext().getTokenString();

    }

}
