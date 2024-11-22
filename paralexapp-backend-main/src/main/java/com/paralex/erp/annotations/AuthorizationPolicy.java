package com.paralex.erp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizationPolicy {
    RequiredAuthorizationRecord[] records();
    Class<?> bodyClass() default Class.class;
    String pathVariablePattern() default "";
    boolean targetInPathVariable() default false;
    boolean targetInBody() default false;
    boolean targetInRequestParam() default false;
    String pathToTarget() default "";
}
