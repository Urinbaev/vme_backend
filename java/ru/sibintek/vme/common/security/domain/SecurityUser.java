package ru.sibintek.vme.common.security.domain;

import lombok.Data;

import java.util.Set;

@Data(staticConstructor = "of")
public class SecurityUser {

    private final String id;
    private final String email;
    private final String name;
    private final Long companyId;
    private final Set<Role> roles;

    public static SecurityUser anonymous() {
        return new SecurityUser(null, null, null, null, Set.of(Role.ANONYMOUS));
    }

}
