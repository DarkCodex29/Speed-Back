package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.DataProcesoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.ProcesoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.TipoDocumentoProcesoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.filter.mantenimiento.ProcesoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.MantenimientoProcesoService;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("MantenimientoProcesoService")
public class MantenimientoProcesoServiceImpl implements MantenimientoProcesoService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoProcesoServiceImpl.class.getName());
    private final TipoProcesoRepository tipoProcesoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProcesoRepository procesoRepository;
    private final RolRepository rolRepository;
    private final ParametroRepository parametroRepository;
    private final TipoDocumentoPorProcesoRepository tipoDocumentoPorProcesoRepository;
    private final ResponsableUsuarioRepository responsableUsuarioRepository;
    private final ResponsableRolRepository responsableRolRepository;

    public MantenimientoProcesoServiceImpl(TipoProcesoRepository tipoProcesoRepository,
                                           TipoDocumentoRepository tipoDocumentoRepository,
                                           UsuarioRepository usuarioRepository,
                                           ProcesoRepository procesoRepository,
                                           RolRepository rolRepository,
                                           ParametroRepository parametroRepository,
                                           TipoDocumentoPorProcesoRepository tipoDocumentoPorProcesoRepository,
                                           ResponsableUsuarioRepository responsableUsuarioRepository,
                                           ResponsableRolRepository responsableRolRepository) {
        this.tipoProcesoRepository = tipoProcesoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.procesoRepository = procesoRepository;
        this.rolRepository = rolRepository;
        this.parametroRepository = parametroRepository;
        this.tipoDocumentoPorProcesoRepository = tipoDocumentoPorProcesoRepository;
        this.responsableUsuarioRepository = responsableUsuarioRepository;
        this.responsableRolRepository = responsableRolRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<List<TipoDocumento>> getTiposDocumentos() {
        try {
            return new ResponseEntity<>(tipoDocumentoRepository.getTiposActivos(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<List<Usuario>> obtenerTodosUsuariosActivos() {
        try {
            return new ResponseEntity<>(usuarioRepository.obtenerTodosUsuariosActivos(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public DataProcesoBean find(Integer id) {

        List<TipoDocumentoPorProceso> tipoDocumentoPorProcesos = tipoDocumentoPorProcesoRepository.getTipoDocumentoPorProceso(id);
        List<TipoDocumentoProcesoBean> tipoDocumentoProceso = new ArrayList<>();
        DataProcesoBean procesoBean = new DataProcesoBean();

        ResponsableUsuario responsableUsuario = responsableUsuarioRepository.findById(id);
        ResponsableRol responsableRol = responsableRolRepository.findById(id);

        Usuario usuarioResponsable = usuarioRepository.findById(responsableUsuario != null ? responsableUsuario.getId().getIdUsuario() : null);
        Rol rolResponsable = rolRepository.findById(responsableRol != null ? responsableRol.getId().getIdRol() : null);

        Proceso proceso = procesoRepository.findById(id);

        for (TipoDocumentoPorProceso tipoDocumentoPorProceso : tipoDocumentoPorProcesos) {
            TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(tipoDocumentoPorProceso.getId().getIdTipoDocumento());
            TipoDocumentoProcesoBean tipoDocumentoProcesoBean = new TipoDocumentoProcesoBean();
            tipoDocumentoProcesoBean.setId(tipoDocumento.getId());
            tipoDocumentoProcesoBean.setNombre(tipoDocumento.getNombre());
            tipoDocumentoProcesoBean.setDescripcion(tipoDocumento.getDescripcion());
            tipoDocumentoProcesoBean.setCantidad(tipoDocumentoPorProceso.getCantidad());
            tipoDocumentoProceso.add(tipoDocumentoProcesoBean);
        }

        procesoBean.setId(proceso.getId());
        procesoBean.setNombre(proceso.getNombre());
        procesoBean.setDescripcion(proceso.getDescripcion());
        procesoBean.setTipoProceso(proceso.getTipoProceso());
        procesoBean.setConfidencialidad(parametroRepository.findObtenerPorTipoValor(Constantes.PARAMETRO_CONFIDENCIALIDAD, String.valueOf(proceso.getTipoConfidencialidad())));
        procesoBean.setPlazoDias(String.valueOf(proceso.getPlazo()));
        procesoBean.setConCliente(proceso.getCliente());
        procesoBean.setEstado(proceso.getEstado() == 'A' ? true : false);
        procesoBean.setCreadorResponsable(proceso.isCreadorResponsable());

        procesoBean.setUsuarioResponsable(usuarioResponsable);
        procesoBean.setRolResponsable(rolResponsable);

        procesoBean.setUsuariosParticipantes(proceso.getUsuariosParticipantes());
        procesoBean.setRolesParticipantes(proceso.getRolesParticipantes());
        procesoBean.setRolesProcesos(proceso.getRolesCreadores());
        procesoBean.setTipoDocumentos(tipoDocumentoProceso);
        return procesoBean;
    }

    @Override
    public List<DataProcesoBean> list(ProcesoFilter procesoFilter) {

        List<DataProcesoBean> dataProcesada = new ArrayList<>();
        List<Proceso> procesos = procesoFilter.isOnlyParents()? procesoRepository.listOnlyParents(procesoFilter.getIdTipoProceso()):procesoRepository.list(procesoFilter.getNombre(), procesoFilter.getIdTipoProceso());

        for (Proceso proceso : procesos) {
            DataProcesoBean procesoBean = new DataProcesoBean();
            procesoBean.setId(proceso.getId());
            procesoBean.setNombre(proceso.getNombre());
            procesoBean.setDescripcion(proceso.getDescripcion());
            procesoBean.setTipoProceso(proceso.getTipoProceso());
            procesoBean.setPlazoDias(String.valueOf(proceso.getPlazo()));
            procesoBean.setConCliente(proceso.getCliente());
            procesoBean.setEstado(proceso.getEstado() == 'A' );
            procesoBean.setIdProcesoPadre(proceso.getIdProcesoPadre());
            dataProcesada.add(procesoBean);
        }

        return dataProcesada;
    }

    @Override
    public List<TipoProceso> listTipoProcesos() {
        return tipoProcesoRepository.list();
    }

    @Override
    public List<Parametro> listConfidencialidad() {
        return parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_CONFIDENCIALIDAD);
    }

    @Override
    public List<UsuariosBean> listUsuarios() {

        List<Usuario> usuariosBD = usuarioRepository.list(null, null, null);
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

    @Override
    public List<UsuariosBean> listUsuariosDisponibles(Integer idUsuario) {

        List<Usuario> usuariosBD = usuarioRepository.obtenerNoAsignadosUsuario(idUsuario);
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

    @Override
    public List<TipoDocumento> listTipoDocumento() {
        return tipoDocumentoRepository.list(null, null);
    }

    @Override
    public List<TipoDocumento> listTipoDocumentoDisponibles(Integer idProceso) {
        return tipoDocumentoRepository.obtenerTipoDocumentoNoAsignados(idProceso);
    }

    @Override
    @Transactional
    public ResponseModel save(ProcesoBean procesoBean) {

        ResponseModel responseModel = new ResponseModel();

        //Responsables
        List<Usuario> usuariosResponsables = new ArrayList<>();
        List<Rol> rolesResponsables = new ArrayList<>();

        //Grupos
        List<Usuario> usuariosParticipantes = new ArrayList<>();
        List<Rol> rolesParticipantes = new ArrayList<>();
        List<Rol> rolesProcesos = new ArrayList<>();

        try {
            TipoProceso tipoProceso = tipoProcesoRepository.findById(procesoBean.getIdTipoProceso());
            Parametro parametro = parametroRepository.findById(procesoBean.getIdConfidencialidad());

            Usuario usuarioResponsable = usuarioRepository.findById(procesoBean.getIdUsuarioResponsable() != null ? procesoBean.getIdUsuarioResponsable() : null);
            Rol rolResponsable = rolRepository.findById(procesoBean.getIdRolResponsable() != null ? procesoBean.getIdRolResponsable() : null);

            if (usuarioResponsable != null) {
                usuariosResponsables.add(usuarioResponsable);
            }
            if (rolResponsable != null) {
                rolesResponsables.add(rolResponsable);
            }

            for (Usuario usuario : procesoBean.getUsuariosParticipantes()) {
                Usuario usuarioBD = usuarioRepository.findById(usuario.getId());
                usuariosParticipantes.add(usuarioBD);
            }

            for (Rol rolParticipante : procesoBean.getRolesParticipantes()) {
                Rol rolParticipanteBD = rolRepository.findById(rolParticipante.getId());
                rolesParticipantes.add(rolParticipanteBD);
            }

            for (Rol rolProceso : procesoBean.getRolesProcesos()) {
                Rol rolProcesoBD = rolRepository.findById(rolProceso.getId());
                rolesProcesos.add(rolProcesoBD);
            }

            if (procesoBean.getId() != null) {

                Proceso procesoBD = procesoRepository.findById(procesoBean.getId());
                procesoBD.setNombre(procesoBean.getNombre());
                procesoBD.setDescripcion(procesoBean.getDescripcion());
                procesoBD.setPlazo(Integer.valueOf(procesoBean.getPlazo()));
                procesoBD.setFechaCreacion(new Date());

                procesoBD.setEstado(procesoBean.getEstado() ? 'A' : 'I');
                procesoBD.setCliente(procesoBean.getConCliente());
                procesoBD.setCreadorResponsable(procesoBean.getCreadorResponsable());
                procesoBD.setTipoConfidencialidad(parametro.getValor().charAt(0));
                procesoBD.setTipoProceso(tipoProceso);

                procesoBD.setUsuariosParticipantes(usuariosParticipantes);
                procesoBD.setRolesParticipantes(rolesParticipantes);
                procesoBD.setRolesCreadores(rolesProcesos);

                procesoRepository.save(procesoBD);

                responseModel.setMessage("Campo actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(procesoBD.getId());
            } else {

                Proceso proceso = new Proceso();
                proceso.setNombre(procesoBean.getNombre());
                proceso.setDescripcion(procesoBean.getDescripcion());
                proceso.setPlazo(Integer.valueOf(procesoBean.getPlazo()));
                proceso.setFechaCreacion(new Date());

                proceso.setEstado(procesoBean.getEstado() ? 'A' : 'I');
                proceso.setCliente(procesoBean.getConCliente());
                proceso.setCreadorResponsable(procesoBean.getCreadorResponsable());
                proceso.setTipoConfidencialidad(parametro.getValor().charAt(0));
                proceso.setTipoProceso(tipoProceso);

                proceso.setUsuariosParticipantes(usuariosParticipantes);
                proceso.setRolesParticipantes(rolesParticipantes);
                proceso.setRolesCreadores(rolesProcesos);

                procesoRepository.save(proceso);

                responseModel.setMessage("Campo creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(proceso.getId());
            }

            tipoDocumentoPorProcesoRepository.eliminarTipoDocumentosPorProceso(responseModel.getId());

            for (TipoDocumentoProcesoBean tipoDocumentoProcesoBean : procesoBean.getTipoDocumentos()) {

                TipoDocumentoPorProceso tipoDocumentoPorProceso = new TipoDocumentoPorProceso();
                TipoDocumento tipoDocumentoBD = tipoDocumentoRepository.findById(tipoDocumentoProcesoBean.getId());
                TipoDocumentoPorProcesoPK tipoDocumentoPorProcesoPK = new TipoDocumentoPorProcesoPK();
                tipoDocumentoPorProcesoPK.setIdProceso(responseModel.getId());
                tipoDocumentoPorProcesoPK.setIdTipoDocumento(tipoDocumentoBD.getId());

                tipoDocumentoPorProceso.setId(tipoDocumentoPorProcesoPK);
                tipoDocumentoPorProceso.setCantidad(10);

                tipoDocumentoPorProcesoRepository.save(tipoDocumentoPorProceso);

                LOGGER.info("==================================================================");
                LOGGER.info("TIPO POR DOCUMENTO Y PROCESO = " + tipoDocumentoPorProceso.getId());
                LOGGER.info("==================================================================");
            }

            for (Usuario usuario : usuariosResponsables) {

                responsableUsuarioRepository.eliminarResponsableUsuariosPorProceso(responseModel.getId());

                ResponsableUsuario responsableUsuario = new ResponsableUsuario();
                ResponsableUsuarioPK responsableUsuarioPK = new ResponsableUsuarioPK();
                responsableUsuarioPK.setIdUsuario(usuario.getId());
                responsableUsuarioPK.setIdProceso(responseModel.getId());
                responsableUsuario.setId(responsableUsuarioPK);
                responsableUsuarioRepository.save(responsableUsuario);

                LOGGER.info("==================================================================");
                LOGGER.info("RESPONSABLE USUARIO = " + responsableUsuario.getId());
                LOGGER.info("==================================================================");
            }

            for (Rol rol : rolesResponsables) {

                responsableRolRepository.eliminarResponsableRolesPorProceso(responseModel.getId());

                ResponsableRol responsableRol = new ResponsableRol();
                ResponsableRolPK responsableRolPK = new ResponsableRolPK();
                responsableRolPK.setIdRol(rol.getId());
                responsableRolPK.setIdProceso(responseModel.getId());
                responsableRol.setId(responsableRolPK);
                responsableRolRepository.save(responsableRol);

                LOGGER.info("==================================================================");
                LOGGER.info("RESPONSABLE ROL = " + responsableRol.getId());
                LOGGER.info("==================================================================");
            }

        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}
