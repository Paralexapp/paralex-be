package com.paralex.erp.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    USER,
    SERVICE_PROVIDER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }

}
