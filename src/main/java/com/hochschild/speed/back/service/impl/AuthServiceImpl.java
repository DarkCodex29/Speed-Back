package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.ApplicationConfig;
import com.hochschild.speed.back.model.auth.User;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.repository.speed.ParametroRepository;
import com.hochschild.speed.back.repository.speed.PerfilRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.security.ldap.LdapService;
import com.hochschild.speed.back.service.AuthService;
import com.hochschild.speed.back.service.CustomUserService;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.EncryptUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author HEEDCOM S.A.C.
 * @since 25/02/2019
 */

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = Logger.getLogger(AuthServiceImpl.class.getName());

    private final LdapService ldapService;

    private final CustomUserService customUserService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;

    private final ParametroRepository parametroRepository;

    private final ApplicationConfig applicationConfig;

    @Autowired
    public AuthServiceImpl(LdapService ldapService,
                           UsuarioRepository usuarioRepository,
                           PerfilRepository perfilRepository,
                           ParametroRepository parametroRepository,
                           CustomUserService customUserService,
                           AuthenticationManager authenticationManager,
                           ApplicationConfig applicationConfig,
                           JwtTokenUtil jwtTokenUtil) {
        this.ldapService = ldapService;
        this.customUserService = customUserService;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.parametroRepository = parametroRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.applicationConfig = applicationConfig;
    }

    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, ""));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Override
    public User login(String username, String password, Integer idPerfil) {
        User user = null;

        // Si los datos del Usuario se encuentran en el servidor LDAP
        try {
            if (ldapService.getAuthEnable() && !ldapService.getWindowsAuthEnable() && this.ldapService.authUser(username, password)) {
                // Se obtienen los datos del usuario
                LOGGER.debug("El usuario autenticado en el LDAP.");
                authenticate(username, password);
                user = customUserService.getUserByUsernameWithMenu(username, idPerfil);
                if (user != null) {
                    user.setToken(jwtTokenUtil.generateToken(user));
                }
                return user;

                //impl windows ad
            } else if (ldapService.getAuthEnable() && ldapService.getWindowsAuthEnable() && this.ldapService.authUserActiveDirectory(username, password)) {
                LOGGER.debug("Autenticando el usuario contra Active Directory");
                /*Active Directory*/
                user = customUserService.getUserByUsername(username);
                if (user == null) {
                    //El usuario existe en AD pero no en BD. Se crea el usuario con los datos mínimos

                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setUsuario(username);
                    nuevoUsuario.setNombres(username);
                    nuevoUsuario.setClave(EncryptUtil.encrypt(EncryptUtil.DEFAULT_ENCRYPTION_KEY, password));
                    nuevoUsuario.setCorreo("");
                    nuevoUsuario.setEstado(Constantes.ESTADO_ACTIVO);
                    nuevoUsuario.setFechaCreacion(new Date());

                    Integer idPerfilUF = Integer.parseInt(parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_ID_PERFIL_USUARIOFINAL).get(0).getValor());
                    Perfil perfilEstandar = perfilRepository.findById(idPerfilUF);
                    List<Perfil> listaPerfil = new ArrayList<>();
                    listaPerfil.add(perfilEstandar);
                    nuevoUsuario.setPerfiles(listaPerfil);
                    usuarioRepository.save(nuevoUsuario);
                }

                authenticate(username, password);
                user = customUserService.getUserByUsernameWithMenu(username, idPerfil);
                if (user != null) {
                    user.setToken(jwtTokenUtil.generateToken(user));
                }
                return user;

            } else if(!ldapService.getAuthEnable()){ // El usuario no está registrado en el LDAP o la contraseña es incorrecta
                // Verificamos que el usuario exista en la BD,
                authenticate(username, password);
                user = customUserService.getUserByUsernameWithMenu(username, idPerfil);
                if (user != null) {
                    user.setToken(jwtTokenUtil.generateToken(user));
                }
                return user;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.error("Usuario o clave incorrectos.");
        return null;
    }

    @Override
    public User loginExternal(String key) {
        User user = null;
        try {
            String username= EncryptUtil.decrypt(EncryptUtil.DEFAULT_ENCRYPTION_KEY, key);
            authenticate(username, "");
            user = customUserService.getUserByUsernameWithMenu(username, null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        if (user != null) {
            user.setToken(jwtTokenUtil.generateToken(user));
        }
        return user;
    }

    @Override
    public User loginExternalMobile(String key) {
        User user = null;
        try {
            //String encryptedKey = URLDecoder.decode(key, "UTF-8");
            String username= EncryptUtil.decrypt(EncryptUtil.DEFAULT_ENCRYPTION_KEY, key);
            authenticate(username, "");
            user = customUserService.getUserByUsernameWithMenuMobile(username, null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        if (user != null) {
            user.setToken(jwtTokenUtil.generateToken(user));
        }
        return user;
    }

    @Override
    public String generateExternalLink(String usuario, Integer idTraza) {
        String externalLink="";
        try {
            String key= EncryptUtil.encrypt(EncryptUtil.DEFAULT_ENCRYPTION_KEY, usuario);
            externalLink=applicationConfig.getFrontUrl()+"#/login-external/"+key+"/"+idTraza;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return externalLink;
    }
}