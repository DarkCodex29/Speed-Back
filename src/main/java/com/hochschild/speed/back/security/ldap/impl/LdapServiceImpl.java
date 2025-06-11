package com.hochschild.speed.back.security.ldap.impl;

import com.hochschild.speed.back.config.LdapConfig;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.security.ldap.LdapService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

@Service
public class LdapServiceImpl implements LdapService {

    private static final Logger LOGGER = Logger.getLogger(LdapServiceImpl.class.getName());

    private final LdapConfig ldapConfig;

    @Autowired
    public LdapServiceImpl(LdapConfig ldapConfig) {
        this.ldapConfig = ldapConfig;
    }

    @Override
    public boolean getAuthEnable() {
        return ldapConfig.getAuthEnable();
    }

    @Override
    public boolean getWindowsAuthEnable() {
        return ldapConfig.getLogueoAlternativo();
    }

    public boolean authUser(String username, String password) {
        Hashtable<String,String> connection=new Hashtable<>();
        String url=ldapConfig.getUrl();
        connection.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        connection.put(Context.PROVIDER_URL,url);
        connection.put(Context.SECURITY_AUTHENTICATION,"simple");
        connection.put(Context.SECURITY_PRINCIPAL,username+"@"+ldapConfig.getDomain());
        connection.put(Context.SECURITY_CREDENTIALS,password);
        try{
            new InitialDirContext(connection);
            LOGGER.debug("Conectado al servidor Ldap: " + url);
            LOGGER.info("Usuario " + username + " autenticado correctamente en el LDAP;");
            return true;
        }
        catch(AuthenticationException ae){
            LOGGER.warn("Usuario o Contraseña Incorrecta. No se pudo conectar al servidor LDAP.",ae);
        }
        catch(NamingException e){
            LOGGER.error("No se pudo conectar con el servidor",e);
        }
        catch(Exception e){
            LOGGER.error("Excepcion",e);
        }
        return false;
    }

    public boolean authUserActiveDirectory(String username,String password){

        Hashtable<String, String> env = getEnvActiveDirectory(username, password);

        try{
            new InitialDirContext(env);
            return true;
        }
        catch(AuthenticationException e){
            LOGGER.warn("Usuario o Contraseña Incorrecta.",e);
        }
        catch(NamingException e){
            LOGGER.error("No se pudo conectar con el servidor",e);
        }
        catch(Exception e){
            LOGGER.error("Excepcion",e);
        }

        return false;
    }

    private Hashtable<String, String> getEnvActiveDirectory(String username, String password) {
        Hashtable<String,String> env=new Hashtable<>(11);
        String provider= ldapConfig.getWindowsUrl();
        String securityA=ldapConfig.getWindowsAutenticacion();
        String securityPrincipal= username +"@"+ldapConfig.getWindowsDomain();

        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL,provider);
        env.put(Context.SECURITY_AUTHENTICATION,securityA);
        env.put(Context.SECURITY_PRINCIPAL,securityPrincipal);
        env.put(Context.SECURITY_CREDENTIALS, password);
        return env;
    }
}