package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.TipoAlertaBean;
import com.hochschild.speed.back.model.domain.speed.TipoAlerta;
import com.hochschild.speed.back.model.filter.mantenimiento.TipoAlertaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.TipoAlertaRepository;
import com.hochschild.speed.back.service.MantenimientoTipoAlertaService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MantenimientoTipoAlertaServiceImpl implements MantenimientoTipoAlertaService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoTipoAlertaServiceImpl.class.getName());
    private final TipoAlertaRepository tipoAlertaRepository;

    public MantenimientoTipoAlertaServiceImpl(TipoAlertaRepository tipoAlertaRepository) {
        this.tipoAlertaRepository = tipoAlertaRepository;
    }

    @Override
    public TipoAlerta find(Integer id) {
        return tipoAlertaRepository.findById(id);
    }

    @Override
    public List<TipoAlerta> list(TipoAlertaFilter tipoAlertaFilter) {
        return tipoAlertaRepository.list(tipoAlertaFilter.getNombre(), tipoAlertaFilter.getPorcentaje());
    }

    @Override
    public ResponseModel save(TipoAlertaBean tipoAlertaBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            if (tipoAlertaBean.getId() != null){
                TipoAlerta tipoAlertaBD = tipoAlertaRepository.findById(tipoAlertaBean.getId());
                tipoAlertaBD.setFechaCreacion(new Date());
                tipoAlertaBD.setImagen(tipoAlertaBean.getImagen());
                tipoAlertaBD.setNombre(tipoAlertaBean.getNombre());
                tipoAlertaBD.setFechaCreacion(new Date());
                tipoAlertaBD.setDefecto(tipoAlertaBean.getDefecto());
                tipoAlertaBD.setPorcentaje(tipoAlertaBean.getPorcentajeIntervalo());
                tipoAlertaRepository.save(tipoAlertaBD);
                responseModel.setMessage("Tipo Alerta actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(tipoAlertaBD.getId());

            }else{
                TipoAlerta tipoAlerta = new TipoAlerta();
                tipoAlerta.setFechaCreacion(new Date());
                tipoAlerta.setImagen(tipoAlertaBean.getImagen());
                tipoAlerta.setNombre(tipoAlertaBean.getNombre());
                tipoAlerta.setFechaCreacion(new Date());
                tipoAlerta.setDefecto(tipoAlertaBean.getDefecto());
                tipoAlerta.setPorcentaje(tipoAlertaBean.getPorcentajeIntervalo());
                tipoAlertaRepository.save(tipoAlerta);
                responseModel.setMessage("Tipo Alerta creada");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(tipoAlerta.getId());
            }

        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }

        return responseModel;
    }
}