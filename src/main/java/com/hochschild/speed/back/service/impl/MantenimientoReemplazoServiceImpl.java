package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.ProcesoReeemplazoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.ReemplazoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.domain.speed.Proceso;
import com.hochschild.speed.back.model.domain.speed.Reemplazo;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.filter.mantenimiento.ReemplazoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.ProcesoRepository;
import com.hochschild.speed.back.repository.speed.ReemplazoRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.MantenimientoReemplazoService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("MantenimientoReemplazoService")
public class MantenimientoReemplazoServiceImpl implements MantenimientoReemplazoService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoReemplazoServiceImpl.class.getName());
    private final ReemplazoRepository reemplazoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProcesoRepository procesoRepository;
    public MantenimientoReemplazoServiceImpl(ReemplazoRepository reemplazoRepository,
                                             UsuarioRepository usuarioRepository,
                                             ProcesoRepository procesoRepository) {
        this.reemplazoRepository = reemplazoRepository;
        this.usuarioRepository = usuarioRepository;
        this.procesoRepository = procesoRepository;
    }

    @Override
    public Reemplazo find(Integer id) {

        Reemplazo reemplazoBD = reemplazoRepository.findById(id);
        List<Proceso> procesosFormateados = new ArrayList<>();

        for (Proceso proceso : reemplazoBD.getProcesos()) {
            Proceso procesoFormat = new Proceso();
            procesoFormat.setId(proceso.getId());
            procesoFormat.setNombre(proceso.getNombre());
            procesoFormat.setDescripcion(proceso.getDescripcion());
            procesosFormateados.add(procesoFormat);
        }
        reemplazoBD.setProcesos(procesosFormateados);
        return reemplazoBD;
    }

    @Override
    public List<Reemplazo> list(ReemplazoFilter reemplazoFilter) {

        List<Reemplazo> reemplazosBD = reemplazoRepository.list(reemplazoFilter.getReemplazante(),reemplazoFilter.getReemplazado());
        for (Reemplazo reemplazo : reemplazosBD) {
            List<Proceso> procesosFormateados = new ArrayList<>();
            for (Proceso proceso : reemplazo.getProcesos()) {
                Proceso procesoFormat = new Proceso();
                procesoFormat.setId(proceso.getId());
                procesoFormat.setNombre(proceso.getNombre());
                procesoFormat.setDescripcion(proceso.getDescripcion());
                procesosFormateados.add(procesoFormat);
            }
            reemplazo.setProcesos(procesosFormateados);
        }
        return reemplazosBD;
    }

    @Override
    public List<UsuariosBean> listUsuarios() {

        List<UsuariosBean> usuarios = new ArrayList<>();
        List<Usuario> usuariosBD = usuarioRepository.list(null,null,null);

        for (Usuario usuario : usuariosBD) {
            UsuariosBean usuarioBean = new UsuariosBean();
            usuarioBean.setId(usuario.getId());
            usuarioBean.setUsuario(usuario.getNombreCompleto());
            usuarioBean.setNombres(usuario.getNombres());
            usuarioBean.setApellidos(usuario.getNombres());
            usuarioBean.setArea(null);
            usuarioBean.setNombreCompleto(usuario.getNombreCompleto());
            usuarios.add(usuarioBean);
        }

        return usuarios;
    }

    @Override
    public List<ProcesoReeemplazoBean> listProcesos() {

        List<ProcesoReeemplazoBean> procesos = new ArrayList<>();
        List<Proceso> procesoBD = procesoRepository.obtenerProcesosActivos();

        for (Proceso proceso : procesoBD) {
            ProcesoReeemplazoBean procesoReeemplazoBean = new ProcesoReeemplazoBean();
            procesoReeemplazoBean.setId(proceso.getId());
            procesoReeemplazoBean.setNombre(proceso.getNombre());
            procesoReeemplazoBean.setDescripcion(proceso.getDescripcion());
            procesoReeemplazoBean.setEstado(String.valueOf(proceso.getEstado()));
            procesoReeemplazoBean.setTipoProceso(proceso.getTipoProceso());
            procesos.add(procesoReeemplazoBean);
        }

        return procesos;
    }

    @Override
    public List<ProcesoReeemplazoBean> listProcesosDisponibles(Integer id) {

        List<ProcesoReeemplazoBean> procesos = new ArrayList<>();
        List<Proceso> procesoBD = procesoRepository.obtenerNoAsignadosUsuario(id);

        for (Proceso proceso : procesoBD) {
            ProcesoReeemplazoBean procesoReeemplazoBean = new ProcesoReeemplazoBean();
            procesoReeemplazoBean.setId(proceso.getId());
            procesoReeemplazoBean.setNombre(proceso.getNombre());
            procesoReeemplazoBean.setDescripcion(proceso.getDescripcion());
            procesoReeemplazoBean.setEstado(String.valueOf(proceso.getEstado()));
            procesoReeemplazoBean.setTipoProceso(proceso.getTipoProceso());
            procesos.add(procesoReeemplazoBean);
        }

        return procesos;
    }

    @Override
    public List<UsuariosBean> listJefes(Integer id) {

        List<UsuariosBean> usuarios = new ArrayList<>();
        List<Usuario> usuariosBD = usuarioRepository.listJefes(id);

        for (Usuario usuario : usuariosBD) {
            UsuariosBean usuarioBean = new UsuariosBean();
            usuarioBean.setId(usuario.getId());
            usuarioBean.setUsuario(usuario.getUsuario());
            usuarioBean.setNombres(usuario.getNombres());
            usuarioBean.setApellidos(usuario.getApellidos());
            usuarioBean.setArea(null);
            usuarioBean.setNombreCompleto(usuario.getNombreCompleto());
            usuarios.add(usuarioBean);
        }

        return usuarios;
    }

    @Override
    public ResponseModel save(ReemplazoBean reemplazoBean) {

        ResponseModel responseModel = new ResponseModel();
        List<Proceso> procesos = new ArrayList<>();

        try {
            Usuario reemplazado = usuarioRepository.findById(reemplazoBean.getIdReemplazado());
            Usuario reemplazante = usuarioRepository.findById(reemplazoBean.getIdReemplazante());

            for (ProcesoReeemplazoBean procesoReeemplazoBean : reemplazoBean.getProcesos()) {
                Proceso procesoBD = procesoRepository.findById(procesoReeemplazoBean.getId());
                procesos.add(procesoBD);
            }

            if (reemplazoBean.getId() != null){
                Reemplazo reemplazoBD = reemplazoRepository.findById(reemplazoBean.getId());
                reemplazoBD.setDesde(reemplazoBean.getFechaDesde());
                reemplazoBD.setHasta(reemplazoBean.getFechaHasta());
                reemplazoBD.setFechaCreacion(new Date());
                reemplazoBD.setReemplazado(reemplazado);
                reemplazoBD.setReemplazante(reemplazante);
                reemplazoBD.setProcesos(procesos);
                reemplazoRepository.save(reemplazoBD);
                responseModel.setMessage("Reemplazo actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(reemplazoBD.getId());
            }else{
                Reemplazo reemplazo = new Reemplazo();
                reemplazo.setDesde(reemplazoBean.getFechaDesde());
                reemplazo.setHasta(reemplazoBean.getFechaHasta());
                reemplazo.setFechaCreacion(new Date());
                reemplazo.setReemplazado(reemplazado);
                reemplazo.setReemplazante(reemplazante);
                reemplazo.setProcesos(procesos);
                reemplazoRepository.save(reemplazo);
                responseModel.setMessage("Reemplazo creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(reemplazo.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}