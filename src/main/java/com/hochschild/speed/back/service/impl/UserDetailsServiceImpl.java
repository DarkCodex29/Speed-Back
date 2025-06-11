package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.dao.OpcionSeguridadDao;
import com.hochschild.speed.back.model.auth.Opcion;
import com.hochschild.speed.back.model.auth.SubOpcion;
import com.hochschild.speed.back.model.auth.User;
import com.hochschild.speed.back.model.domain.auth.PuestoPorUsuario;
import com.hochschild.speed.back.model.domain.auth.PuestoPorUsuarioExterno;
import com.hochschild.speed.back.model.domain.speed.SistemaOpciones;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.repository.auth.PuestoPorUsuarioExternoRepository;
import com.hochschild.speed.back.repository.auth.PuestoPorUsuarioRepository;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.CustomUserService;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HEEDCOM S.A.C.
 * @since 25/02/2019
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService, CustomUserService {

    private static final Logger LOGGER = Logger.getLogger(UserDetailsServiceImpl.class.getName());

    private final BusquedaRepository busquedaRepository;
    private final SistemaOpcionesRepository sistemaOpcionesRepository;

    private final UsuarioRepository usuarioRepository;

    private final ParametroRepository parametroRepository;

    private final PuestoPorUsuarioExternoRepository puestoPorUsuarioExternoRepository;

    private final PuestoPorUsuarioRepository puestoPorUsuarioRepository;

    private final RolRepository rolRepository;

    private final OpcionSeguridadDao opcionSeguridadDao;

    private final CydocConfig cydocConfig;

    @Autowired
    public UserDetailsServiceImpl(SistemaOpcionesRepository sistemaOpcionesRepository,
                                  BusquedaRepository busquedaRepository,
                                  UsuarioRepository usuarioRepository,
                                  PuestoPorUsuarioRepository puestoPorUsuarioRepository,
                                  PuestoPorUsuarioExternoRepository puestoPorUsuarioExternoRepository,
                                  ParametroRepository parametroRepository, RolRepository rolRepository, OpcionSeguridadDao opcionSeguridadDao,
                                  CydocConfig cydocConfig) {
        this.sistemaOpcionesRepository = sistemaOpcionesRepository;
        this.busquedaRepository = busquedaRepository;
        this.usuarioRepository = usuarioRepository;
        this.parametroRepository = parametroRepository;
        this.puestoPorUsuarioExternoRepository = puestoPorUsuarioExternoRepository;
        this.puestoPorUsuarioRepository = puestoPorUsuarioRepository;
        this.rolRepository = rolRepository;
        this.opcionSeguridadDao = opcionSeguridadDao;
        this.cydocConfig = cydocConfig;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsuario(), user.getPassword(),
                new ArrayList<>());
    }

    @Override
    public User getUserByUsername(String username) {
        User user = null;

        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario != null) {
            user = new User();
            user.setUsuario(username);
            user.setPassword("");
        }
        return user;
    }

    @Override
    public User getUserByUsernameWithMenu(String username, Integer idPerfil) {

        User user = null;
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario != null) {
            user = new User(usuario);
        }
        if (user != null) {
            if (idPerfil == null) {
                idPerfil = Integer.parseInt(parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_ID_PERFIL_USUARIOFINAL).get(0).getValor());
            }
            user.setIdPerfil(idPerfil);
            user.setRolesUsuario(rolRepository.obtenerAsignadosUsuarioString(usuario.getId()));
            user.setListaOpciones(getOpcionesByUsername(user.getUsuario()));
            user.setBusquedasGuardada(busquedaRepository.listarBusquedasGuardadas(user.getIdUsuario()));
            if (cydocConfig.getScaActivo()) {
                setAreaAndPuesto(user);
            }
        }

        return user;
    }


    @Override
    public User getUserByUsernameWithMenuMobile(String username, Integer idPerfil) {

        User user = null;
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario != null) {
            user = new User(usuario);
        }
        if (user != null) {
            if (idPerfil == null) {
                idPerfil = Integer.parseInt(parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_ID_PERFIL_USUARIOFINAL).get(0).getValor());
            }
            user.setIdPerfil(idPerfil);
            user.setRolesUsuario(rolRepository.obtenerAsignadosUsuarioString(usuario.getId()));
            user.setListaOpciones(getOpcionesByUsernameMobile(user.getUsuario()));
            user.setBusquedasGuardada(busquedaRepository.listarBusquedasGuardadas(user.getIdUsuario()));
            if (cydocConfig.getScaActivo()) {
                setAreaAndPuesto(user);
            }
        }

        return user;
    }

    private void setAreaAndPuesto(User user) {

        if (isUsuarioExterno(user.getUsuario())) {
            List<PuestoPorUsuarioExterno> result = puestoPorUsuarioExternoRepository.findByIdUsuario(user.getUsuario());
            PuestoPorUsuarioExterno puestoPorUsuarioExterno = (result != null && !result.isEmpty()) ? result.get(0) : null;
            if (puestoPorUsuarioExterno != null) {
                user.setArea(Constantes.NOMBRE_PUESTO_EXTERNO);
                user.setPuesto("");
            }
        } else {
            List<PuestoPorUsuario> result = puestoPorUsuarioRepository.findByIdUsuarioAndActivo(user.getUsuario(), "S");
            PuestoPorUsuario puestoPorUsuario = (result != null && !result.isEmpty()) ? result.get(0) : null;
            if (puestoPorUsuario != null) {
                user.setArea(puestoPorUsuario.getAreaUsuario());
                user.setPuesto(puestoPorUsuario.getPuestoUsuario());
            }
        }

    }

    private List<Opcion> getOpcionesByUsernameMobile(String username) {
        List<Opcion> opciones = new ArrayList<>();
        Opcion opcion = new Opcion();

        opcion.setId(64L);
        opcion.setNombre("Usuario Final");
        opcion.setLinkOpcion(null);

        List<SubOpcion> subOpciones = new ArrayList<>();

        SubOpcion subOpcion = new SubOpcion();
        subOpcion.setId(1013L);
        subOpcion.setNombre("Pendientes de visar");
        subOpcion.setLinkOpcion("final-user/inbox");
        subOpciones.add(subOpcion);

        opcion.setSubOpciones(subOpciones);
        opciones.add(opcion);
        return opciones;
    }

    private List<Opcion> getOpcionesByUsername(String username) {

        List<Long> idOpciones = opcionSeguridadDao.getOpcionesSeguridad(this.cydocConfig.getScaIdAplicacion(), username);

        List<SistemaOpciones> sistemaOpcionesHeaders;

        List<SistemaOpciones> sistemaOpciones;

        if (idOpciones == null || idOpciones.isEmpty()) {

            idOpciones = opcionSeguridadDao.getOpcionesSinUsuario(this.cydocConfig.getScaIdAplicacion(), parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_CODIGO_ROL_DEFAULT).get(0).getValor());

        }

//        idOpciones.add(1081L);
//        idOpciones.add(1082L);
//        idOpciones.add(1083L);
        LOGGER.info("gg: " + idOpciones);

        List<Long> idOpcionesHeaders = sistemaOpcionesRepository.findHeadersByPermissions(idOpciones, "S");

        sistemaOpcionesHeaders = sistemaOpcionesRepository.findByPermissionsHeaders(idOpcionesHeaders, "S");

        sistemaOpciones = sistemaOpcionesRepository.findByPermissionsChilds(idOpciones, "S");

        List<Opcion> opciones = new ArrayList<>();
        for (SistemaOpciones sistemaOpcionHeader : sistemaOpcionesHeaders) {

            Opcion opcion = new Opcion();

            opcion.setId(sistemaOpcionHeader.getIdOpcion());
            opcion.setNombre(sistemaOpcionHeader.getOpcion());
            opcion.setLinkOpcion(sistemaOpcionHeader.getLinkOpcion());

            List<SubOpcion> subOpciones = new ArrayList<>();

            for (SistemaOpciones sistemaOpcion : sistemaOpciones) {
                if (sistemaOpcion.getIdPadre() != null
                        && sistemaOpcion.getIdPadre().equals(sistemaOpcionHeader.getIdOpcion())) {

                    SubOpcion subOpcion = new SubOpcion();
                    subOpcion.setId(sistemaOpcion.getIdOpcion());
                    subOpcion.setNombre(sistemaOpcion.getOpcion());
                    subOpcion.setLinkOpcion(sistemaOpcion.getLinkOpcion());
                    subOpciones.add(subOpcion);
                    
                }
            }
            
           
                    
            opcion.setSubOpciones(subOpciones);
            
            
            for (SubOpcion sistemaOpcionChild : opcion.getSubOpciones()) {
                List<SubOpcion> subOpcionesChild = new ArrayList<>();
                for (SistemaOpciones sistemaOpcion : sistemaOpciones) {
                    if (sistemaOpcion.getIdPadre() != null
                            && sistemaOpcion.getIdPadre().equals(sistemaOpcionChild.getId())) {

                        SubOpcion subOpcion = new SubOpcion();
                        subOpcion.setId(sistemaOpcion.getIdOpcion());
                        subOpcion.setNombre(sistemaOpcion.getOpcion());
                        subOpcion.setLinkOpcion(sistemaOpcion.getLinkOpcion());
                        subOpcionesChild.add(subOpcion);

                    }
                }
                sistemaOpcionChild.setSubOpciones(subOpcionesChild);
            }
           
            
            opciones.add(opcion);

        }
        
//        Opcion opcion3 = opciones.get(2);
//        Opcion opcion4 = new Opcion();
//        opcion4.setId(1048L);
//        opcion4.setNombre("Registrar Contrato Manual");
//        opcion4.setLinkOpcion("final-user/register-manual-contract");
//        
//        List<SubOpcion> opciones4 = opcion3.getSubOpciones();
//        SubOpcion opciones5 = opciones4.get(2);
//        List<SubOpcion> opciones6 = new ArrayList<>();
//        opciones6.add(opcion4);
//        opciones5.setSubOpciones(opciones6);
//        
        return opciones;
    }

    private boolean isUsuarioExterno(String idUser) {
        return idUser.toLowerCase().contains(".ext");
    }
}