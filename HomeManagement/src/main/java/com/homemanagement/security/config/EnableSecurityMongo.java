package com.homemanagement.security.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ HomeManagementConfiguration.class })
public @interface EnableSecurityMongo {
}
