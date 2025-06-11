package com.hochschild.speed.back.security.ldap;

public interface LdapService {

    boolean getWindowsAuthEnable();
    boolean getAuthEnable();
    boolean authUser(String username, String password);

    boolean authUserActiveDirectory(String usuario,String password);
}
