package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.dao.BotonesDao;
import com.hochschild.speed.back.model.bean.mantenimiento.RepresentanteCompBean;
import com.hochschild.speed.back.model.bean.mantenimiento.RepresentantesBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.domain.speed.Boton;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.domain.speed.RepresentanteComp;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.domain.speed.utils.UsuarioRepCompDTO;
import com.hochschild.speed.back.model.filter.mantenimiento.RepCompaniaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.ParametroRepository;
import com.hochschild.speed.back.repository.speed.PerfilRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepresentanteCompaniaRepository;
import com.hochschild.speed.back.service.MantenimientoRepCompaniaService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("MantenimientoRepCompaniaService")
public class MantenimientoRepCompaniaServiceImpl implements MantenimientoRepCompaniaService {
    private Logger LOGGER = LoggerFactory.getLogger(MantenimientoRepCompaniaServiceImpl.class);

    private final UsuarioRepository usuarioRepository;
    private final ParametroRepository parametroRepository;
    private final PerfilRepository perfilRepository;
    private final UsuarioRepresentanteCompaniaRepository usuarioRepresentanteCompaniaRepository;
    private final BotonesDao botonesDao;

    @Autowired
    public MantenimientoRepCompaniaServiceImpl(UsuarioRepository usuarioRepository,
                                               ParametroRepository parametroRepository,
                                               PerfilRepository perfilRepository,
                                               UsuarioRepresentanteCompaniaRepository usuarioRepresentanteCompaniaRepository,
                                               BotonesDao botonesDao) {
        this.usuarioRepository = usuarioRepository;
        this.parametroRepository = parametroRepository;
        this.perfilRepository = perfilRepository;
        this.usuarioRepresentanteCompaniaRepository = usuarioRepresentanteCompaniaRepository;
        this.botonesDao = botonesDao;
    }

    @Override
    public RepresentanteComp obtenerPorId(Integer id) {
        return usuarioRepresentanteCompaniaRepository.findById(id);
    }

    @Override
    @Transactional
    public Integer guardar(UsuarioRepCompDTO usuarioRepCompDTO) {
        try {
            if (usuarioRepCompDTO.getIdRepComp() == null) {
                Usuario userRep = usuarioRepository.obtenerPorId(usuarioRepCompDTO.getId());
                RepresentanteComp repComp = new RepresentanteComp();
                repComp.setRepresentante(userRep);
                repComp.setCorreo(usuarioRepCompDTO.getCorreo());
                if (usuarioRepCompDTO.getEstado() == null) {
                    repComp.setEstado(Constantes.ESTADO_REPCOMP_INACTIVO);
                } else {
                    repComp.setEstado(usuarioRepCompDTO.getEstado());
                }
                repComp.setNroDocumento(usuarioRepCompDTO.getNroDocumento());
                usuarioRepresentanteCompaniaRepository.save(repComp);
                return repComp.getId();
            } else {
                RepresentanteComp existente = usuarioRepresentanteCompaniaRepository.findById(usuarioRepCompDTO.getIdRepComp());
                existente.setRepresentante(usuarioRepository.obtenerPorId(usuarioRepCompDTO.getId()));
                existente.setNroDocumento(usuarioRepCompDTO.getNroDocumento());
                if (usuarioRepCompDTO.getEstado() == null) {
                    existente.setEstado(Constantes.ESTADO_REPCOMP_INACTIVO);
                } else {
                    existente.setEstado(usuarioRepCompDTO.getEstado());
                }
                existente.setCorreo(usuarioRepCompDTO.getCorreo());
                usuarioRepresentanteCompaniaRepository.save(existente);
                return existente.getId();
            }
        } catch (Exception e) {
            System.out.println("Registro fallido");
            e.printStackTrace();

            return 0;
        }
    }

    @Override
    public List<Boton> getBotones(Usuario usu) {
        Integer idPerfil = Integer.parseInt(parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_ID_PERFIL_USUARIOFINAL).get(0).getValor());
        Perfil perfil = perfilRepository.findById(idPerfil);
        return botonesDao.buscarPorPerfilGrid(perfil, Constantes.GRID_REPRESENTANTE_COMP);
    }

    @Override
    public List<RepresentanteComp> obtenerActivos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Usuario> obtenerUsuarioPorAutocomplete(String term) {
        return usuarioRepository.buscarUsuariosActivosPorNombre(AppUtil.quitarAcentos(term.trim().toLowerCase()) + "%");
    }

    @Override
    public RepresentanteComp find(Integer id) {
        return usuarioRepresentanteCompaniaRepository.findById(id);
    }

    @Override
    public List<RepresentantesBean> list(RepCompaniaFilter repCompaniaFilter) {

        List<RepresentantesBean> representantes = new ArrayList<>();
        List<RepresentanteComp> representanteCompList = usuarioRepresentanteCompaniaRepository.list(repCompaniaFilter.getNombres(),
                                                                                                    repCompaniaFilter.getApellidos(),
                                                                                                    repCompaniaFilter.getNumeroDocumento(),
                                                                                                    repCompaniaFilter.getCorreo());

        for (RepresentanteComp representanteComp :representanteCompList) {

            RepresentantesBean representantesBean = new RepresentantesBean();

            representantesBean.setId(representanteComp.getId());
            representantesBean.setNombres(representanteComp.getRepresentante() != null ?  representanteComp.getRepresentante().getNombres() : null);
            representantesBean.setApellidos(representanteComp.getRepresentante() != null ?  representanteComp.getRepresentante().getApellidos() : null);
            representantesBean.setNumeroDocumento(representanteComp.getNroDocumento());
            representantesBean.setCorreo(representanteComp.getCorreo());

            representantes.add(representantesBean);
        }

        return representantes;
    }

    @Override
    public List<UsuariosBean> listUsuarios() {

        List<UsuariosBean> usuarios = new ArrayList<>();
        List<Usuario> usuariosBD = usuarioRepository.list(null,null,null);

        for (Usuario usuario : usuariosBD) {
            UsuariosBean usuarioBean = new UsuariosBean();
            usuarioBean.setId(usuario.getId());
            usuarioBean.setUsuario(usuario.getNombreCompleto());
            usuarioBean.setNombres(null);
            usuarioBean.setApellidos(null);
            usuarioBean.setArea(null);
            usuarios.add(usuarioBean);
        }

        return usuarios;
    }

    @Override
    public ResponseModel save(RepresentanteCompBean representantesCompBean) {

        ResponseModel responseModel = new ResponseModel();

        try {

            Usuario usuario = usuarioRepository.findById(representantesCompBean.getIdUsuario());

            if (representantesCompBean.getId() != null){

                RepresentanteComp representanteCompBD = usuarioRepresentanteCompaniaRepository.findById(representantesCompBean.getId());
                representanteCompBD.setRepresentante(usuario);
                representanteCompBD.setEstado(representantesCompBean.getEstado() ? "S" : "N");
                representanteCompBD.setCorreo(representantesCompBean.getCorreo());
                representanteCompBD.setNroDocumento(representantesCompBean.getNumeroDocumento());

                usuarioRepresentanteCompaniaRepository.save(representanteCompBD);
                responseModel.setMessage("Representante Compañia actualizada");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(representanteCompBD.getId());
            }else{
                RepresentanteComp representanteComp = new RepresentanteComp();

                representanteComp.setRepresentante(usuario);
                representanteComp.setEstado(representantesCompBean.getEstado() ? "S" : "N");
                representanteComp.setCorreo(representantesCompBean.getCorreo());
                representanteComp.setNroDocumento(representantesCompBean.getNumeroDocumento());

                usuarioRepresentanteCompaniaRepository.save(representanteComp);
                responseModel.setMessage("Representante Compañia creada");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(representanteComp.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}