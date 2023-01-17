package ru.sibintek.vme.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sibintek.vme.common.security.AuthenticationFacade;
import ru.sibintek.vme.common.security.domain.SecurityUser;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationFacade authenticationFacade;

    @GetMapping("/user-info")
    public SecurityUser getUserInfo() {
        return authenticationFacade.getCurrentUser();
    }

}
