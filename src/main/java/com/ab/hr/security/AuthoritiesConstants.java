package com.ab.hr.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";
    public static final String HR_USER = "ROLE_HR_USER";
    public static final String CORP_MANAGER = "ROLE_CORP_MANAGER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
