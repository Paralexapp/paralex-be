package com.paralex.erp.annotations;

public @interface RequiredAuthorizationRecord {
    String resource();
    String status() default "Deny";
    String action() default "Create";
}
