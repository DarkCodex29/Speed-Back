package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.ProcesoBean;
import com.hochschild.speed.back.model.domain.speed.Proceso;
import com.hochschild.speed.back.model.domain.speed.TipoDocumento;
import com.hochschild.speed.back.model.domain.speed.TipoProceso;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.filter.mantenimiento.ProcesoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.ProcesoRepository;
import com.hochschild.speed.back.repository.speed.TipoDocumentoRepository;
import com.hochschild.speed.back.repository.speed.TipoProcesoRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.MantenimientoTipoProcesoService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("MantenimientoTipoProcesoService")
public class MantenimientoTipoProcesoServiceImpl implements MantenimientoTipoProcesoService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoTipoProcesoServiceImpl.class.getName());
    private final TipoProcesoRepository tipoProcesoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProcesoRepository procesoRepository;

    public MantenimientoTipoProcesoServiceImpl(TipoProcesoRepository tipoProcesoRepository,
                                               TipoDocumentoRepository tipoDocumentoRepository,
                                               UsuarioRepository usuarioRepository,
                                               ProcesoRepository procesoRepository) {
        this.tipoProcesoRepository = tipoProcesoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.procesoRepository = procesoRepository;
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
    public Proceso find(Integer id) {
        return procesoRepository.findById(id);
    }

    @Override
    public List<Proceso> list(ProcesoFilter procesoFilter) {
        return procesoFilter.isOnlyParents()?procesoRepository.listOnlyParents(procesoFilter.getIdTipoProceso()) :procesoRepository.list(procesoFilter.getNombre(),procesoFilter.getIdTipoProceso());
    }

    @Override
    public ResponseModel save(ProcesoBean procesoBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            TipoProceso tipoProceso = tipoProcesoRepository.findById(procesoBean.getIdTipoProceso());
            if (procesoBean.getId() != null){
                Proceso procesoBD = procesoRepository.findById(procesoBean.getId());
                //procesoBD.setCliente(procesoBean.getCliente());
                procesoBD.setCreadorResponsable(procesoBean.getCreadorResponsable());
                procesoBD.setDescripcion(procesoBean.getDescripcion());
                //procesoBD.setEstado(procesoBean.getEstado());
                procesoBD.setFechaCreacion(new Date());
                procesoBD.setNombre(procesoBean.getNombre());
                //procesoBD.setNombreIntalio(procesoBean.getNombreIntalio());
                //procesoBD.setPlazo(procesoBean.getPlazo());
                //procesoBD.setTipoConfidencialidad(procesoBean.getTipoConfidencialidad());
                procesoBD.setTipoProceso(tipoProceso);
                procesoRepository.save(procesoBD);
                responseModel.setMessage("Campo actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(procesoBD.getId());
            }
            else{
                Proceso proceso = new Proceso();
                //proceso.setCliente(procesoBean.getCliente());
                proceso.setCreadorResponsable(procesoBean.getCreadorResponsable());
                proceso.setDescripcion(procesoBean.getDescripcion());
                //proceso.setEstado(procesoBean.getEstado());
                proceso.setFechaCreacion(new Date());
                proceso.setNombre(procesoBean.getNombre());
                //proceso.setNombreIntalio(procesoBean.getNombreIntalio());
                //proceso.setPlazo(procesoBean.getPlazo());
                //proceso.setTipoConfidencialidad(procesoBean.getTipoConfidencialidad());
                proceso.setTipoProceso(tipoProceso);
                procesoRepository.save(proceso);
                responseModel.setMessage("Campo creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(proceso.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}
