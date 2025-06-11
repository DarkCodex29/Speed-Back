package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.TipoNotificacionBean;
import com.hochschild.speed.back.model.domain.speed.TipoNotificacion;
import com.hochschild.speed.back.model.filter.mantenimiento.TipoNotificacionFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.TipoNotificacionRepository;
import com.hochschild.speed.back.service.MantenimientoTipoNotificacionService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("MantenimientoTipoNotificacionService")
public class MantenimientoTipoNotificacionServiceImpl implements MantenimientoTipoNotificacionService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoTipoNotificacionServiceImpl.class.getName());
    private final TipoNotificacionRepository tipoNotificacionRepository;
    public MantenimientoTipoNotificacionServiceImpl(TipoNotificacionRepository tipoNotificacionRepository) {
        this.tipoNotificacionRepository = tipoNotificacionRepository;
    }

    @Override
    public TipoNotificacion find(Integer id) {
        return tipoNotificacionRepository.findById(id);
    }

    @Override
    public List<TipoNotificacion> list(TipoNotificacionFilter tipoNotificacionFilter) {
        return tipoNotificacionRepository.list(tipoNotificacionFilter.getNombre());
    }

    @Override
    public ResponseModel save(TipoNotificacionBean tipoNotificacionBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            if (tipoNotificacionBean.getId() != null){
                TipoNotificacion tipoNotificacionBD = tipoNotificacionRepository.findById(tipoNotificacionBean.getId());
                tipoNotificacionBD.setNombre(tipoNotificacionBean.getNombre());
                tipoNotificacionBD.setDescripcion(tipoNotificacionBean.getDescripcion());
                tipoNotificacionBD.setFechaCreacion(new Date());
                tipoNotificacionBD.setCodigo(tipoNotificacionBean.getCodigo());
                tipoNotificacionBD.setEstado(tipoNotificacionBean.getEstado() ? 'A' : 'I');
                tipoNotificacionRepository.save(tipoNotificacionBD);
                responseModel.setMessage("Tipo notificacion actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(tipoNotificacionBD.getId());
            }else{
                TipoNotificacion tipoNotificacion = new TipoNotificacion();
                tipoNotificacion.setNombre(tipoNotificacionBean.getNombre());
                tipoNotificacion.setDescripcion(tipoNotificacionBean.getDescripcion());
                tipoNotificacion.setFechaCreacion(new Date());
                tipoNotificacion.setCodigo(tipoNotificacionBean.getCodigo());
                tipoNotificacion.setEstado(tipoNotificacionBean.getEstado() ? 'A' : 'I');
                tipoNotificacionRepository.save(tipoNotificacion);
                responseModel.setMessage("Tipo de notificacion creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(tipoNotificacion.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}