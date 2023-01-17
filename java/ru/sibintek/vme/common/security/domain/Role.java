package ru.sibintek.vme.common.security.domain;

import java.util.Arrays;

public enum Role {
    ADMIN,
    SUPPORT,
    CLIENT,
    ANONYMOUS;

    public static boolean isPresent(String roleName) {
        return Arrays.stream(Role.values()).anyMatch(role -> role.name().equalsIgnoreCase(roleName));
    }

}
