package com.paralex.erp.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    USER,
    SERVICE_PROVIDER_LAWYER,
    SERVICE_PROVIDER_RIDER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }

}
