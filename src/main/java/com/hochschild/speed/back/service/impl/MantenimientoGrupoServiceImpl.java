package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.GrupoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.GrupoUsuariosBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuarioGrupoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.filter.mantenimiento.GrupoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.HcGrupoRepository;
import com.hochschild.speed.back.repository.speed.HcUsuarioPorGrupoRepository;
import com.hochschild.speed.back.repository.speed.ParametroRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.MantenimientoGrupoService;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("MantenimientoGrupoService")
public class MantenimientoGrupoServiceImpl implements MantenimientoGrupoService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoGrupoServiceImpl.class.getName());
    private final HcGrupoRepository hcGrupoRepository;
    private final HcUsuarioPorGrupoRepository hcUsuarioPorGrupoRepository;
    private final ParametroRepository parametroRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public MantenimientoGrupoServiceImpl(HcGrupoRepository hcGrupoRepository,
                                         HcUsuarioPorGrupoRepository hcUsuarioPorGrupoRepository,
                                         ParametroRepository parametroRepository,
                                         UsuarioRepository usuarioRepository) {
        this.hcGrupoRepository = hcGrupoRepository;
        this.hcUsuarioPorGrupoRepository = hcUsuarioPorGrupoRepository;
        this.parametroRepository = parametroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<HcGrupo> obtenerActivos() {
        return hcGrupoRepository.obtenerActivos();
    }

    @Override
    public GrupoUsuariosBean find(Integer id) {

        GrupoUsuariosBean grupoUsuariosBean = new GrupoUsuariosBean();
        List<UsuarioGrupoBean> usuariosxGrupo = new ArrayList<>();

        try {
            HcGrupo hcGrupo = hcGrupoRepository.findById(id);
            List<HcUsuarioPorGrupo> usuarios = hcUsuarioPorGrupoRepository.findByIdGrupo(id);

            for (HcUsuarioPorGrupo hcUsuarioPorGrupo :usuarios) {
                UsuarioGrupoBean usuarioGrupoBean = new UsuarioGrupoBean();
                usuarioGrupoBean.setId(hcUsuarioPorGrupo.getId().getUsuario().getId());
                usuarioGrupoBean.setUsuario(hcUsuarioPorGrupo.getId().getUsuario().getUsuario());
                usuarioGrupoBean.setNombres(hcUsuarioPorGrupo.getId().getUsuario().getNombres());
                usuarioGrupoBean.setApellidos(hcUsuarioPorGrupo.getId().getUsuario().getApellidos());
                usuarioGrupoBean.setCorreo(hcUsuarioPorGrupo.getId().getUsuario().getCorreo());
                usuariosxGrupo.add(usuarioGrupoBean);
            }
            grupoUsuariosBean.setId(hcGrupo.getId());
            grupoUsuariosBean.setNombre(hcGrupo.getNombre());
            grupoUsuariosBean.setEstado(hcGrupo.getEstado() == 'A' ? true : false);
            grupoUsuariosBean.setTipoGrupo(hcGrupo.getTipoGrupo());
            grupoUsuariosBean.setUsuarios(usuariosxGrupo);

        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return grupoUsuariosBean;
    }

    @Override
    public List<HcGrupo> list(GrupoFilter grupoFilter) {
        return hcGrupoRepository.list(grupoFilter.getNombre());
    }

    @Override
    public List<Parametro> listTipoGrupos() {
        return parametroRepository.obtenerPorTipo(Constantes.TIPO_PARAMETRO_GRUPO);
    }

    @Override
    public List<UsuariosBean> listUsuarios() {

        List<Usuario> usuariosBD = usuarioRepository.list(null,null,null);
        List<UsuariosBean> usuarios = new ArrayList<>();

        for (Usuario usuario : usuariosBD) {
            UsuariosBean usuariosBean = new UsuariosBean();
            usuariosBean.setId(usuario.getId());
            usuariosBean.setNombres(usuario.getNombres());
            usuariosBean.setApellidos(usuario.getApellidos());
            usuariosBean.setUsuario(usuario.getUsuario());
            usuariosBean.setNombreCompleto(usuario.getNombreCompleto());
            usuariosBean.setCorreo(usuario.getCorreo());
            usuarios.add(usuariosBean);
        }
        return usuarios;
    }

    @Transactional
    @Override
    public ResponseModel save(GrupoBean grupoBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            HcGrupo hcGrupoBD = hcGrupoRepository.findById(grupoBean.getId());
            Parametro parametro = parametroRepository.findById(grupoBean.getIdTipoGrupo());

            if (grupoBean.getId() != null) {

                hcGrupoBD.setNombre(grupoBean.getNombre());
                hcGrupoBD.setTipoGrupo(parametro);
                hcGrupoBD.setEstado(grupoBean.getEstado() ? 'A' : 'I');
                hcGrupoRepository.save(hcGrupoBD);
                responseModel.setMessage("Grupo actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(hcGrupoBD.getId());

                hcUsuarioPorGrupoRepository.eliminarUsuariosPorGrupo(grupoBean.getId());

                for (UsuariosBean usuarioBean : grupoBean.getUsuarios()) {

                    HcUsuarioPorGrupo hcUsuarioPorGrupo = new HcUsuarioPorGrupo();
                    HcUsuarioPorGrupoPk hcUsuarioPorGrupoPk = new HcUsuarioPorGrupoPk();

                    Usuario usuario = usuarioRepository.findById(usuarioBean.getId());
                    hcUsuarioPorGrupoPk.setGrupo(hcGrupoBD);
                    hcUsuarioPorGrupoPk.setUsuario(usuario);
                    hcUsuarioPorGrupo.setId(hcUsuarioPorGrupoPk);
                    hcUsuarioPorGrupoRepository.save(hcUsuarioPorGrupo);
                }

            } else {

                HcGrupo hcGrupo = new HcGrupo();

                hcGrupo.setNombre(grupoBean.getNombre());
                hcGrupo.setTipoGrupo(parametro);
                hcGrupo.setEstado(grupoBean.getEstado() ? 'A' : 'I');
                hcGrupoRepository.save(hcGrupo);

                for (UsuariosBean usuarioBean : grupoBean.getUsuarios()) {

                    HcUsuarioPorGrupo hcUsuarioPorGrupo = new HcUsuarioPorGrupo();
                    HcUsuarioPorGrupoPk hcUsuarioPorGrupoPk = new HcUsuarioPorGrupoPk();

                    Usuario usuario = usuarioRepository.findById(usuarioBean.getId());
                    hcUsuarioPorGrupoPk.setGrupo(hcGrupo);
                    hcUsuarioPorGrupoPk.setUsuario(usuario);
                    hcUsuarioPorGrupo.setId(hcUsuarioPorGrupoPk);

                    hcUsuarioPorGrupoRepository.save(hcUsuarioPorGrupo);
                }

                responseModel.setMessage("Grupo creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(hcGrupo.getId());
            }
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}