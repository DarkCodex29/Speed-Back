package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.dao.BotonesDao;
import com.hochschild.speed.back.model.bean.mantenimiento.NumeracionBean;
import com.hochschild.speed.back.model.domain.speed.Area;
import com.hochschild.speed.back.model.domain.speed.Numeracion;
import com.hochschild.speed.back.model.domain.speed.TipoDocumento;
import com.hochschild.speed.back.model.filter.mantenimiento.NumeracionFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.MantenimientoNumeracionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MantenimientoNumeracionService")
public class MantenimientoNumeracionServiceImpl implements MantenimientoNumeracionService {

    private static final Logger LOGGER = Logger.getLogger(MantenimientoNumeracionServiceImpl.class.getName());
    private final AreaRepository areaRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final NumeracionRepository numeracionRepository;
    private final ParametroRepository parametroRepository;
    private final PerfilRepository perfilRepository;
    private final BotonesDao botonesDao;

    @Autowired
    public MantenimientoNumeracionServiceImpl(AreaRepository areaRepository,
                                              TipoDocumentoRepository tipoDocumentoRepository,
                                              NumeracionRepository numeracionRepository,
                                              ParametroRepository parametroRepository,
                                              PerfilRepository perfilRepository,
                                              BotonesDao botonesDao) {
        this.areaRepository = areaRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.numeracionRepository = numeracionRepository;
        this.parametroRepository = parametroRepository;
        this.perfilRepository = perfilRepository;
        this.botonesDao = botonesDao;
    }

    @Override
    public Numeracion find(Integer id) {
        return numeracionRepository.findById(id);
    }

    @Override
    public List<Numeracion> list(NumeracionFilter numeracionFilter) {
        return numeracionRepository.list(numeracionFilter.getNumeroActual(), numeracionFilter.getFormato());
    }

    @Override
    public List<TipoDocumento> listTipoDocumentos() {
        return tipoDocumentoRepository.getTiposActivos();
    }

    @Override
    public List<Area> listAreas() {
        return areaRepository.buscarAreas();
    }

    @Override
    public ResponseModel save(NumeracionBean numeracionBean) {

        ResponseModel responseModel = new ResponseModel();

        try {

            TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(numeracionBean.getIdTipoDocumento());
            Area area = areaRepository.findById(numeracionBean.getIdArea());

            if (numeracionBean.getId() != null){
                Numeracion numeracionBD = numeracionRepository.findById(numeracionBean.getId());
                numeracionBD.setArea(area);
                numeracionBD.setTipoDocumento(tipoDocumento);
                numeracionBD.setTipo(numeracionBean.getTipoNumeracion());
                numeracionBD.setValor(numeracionBean.getNumeroActual());
                numeracionBD.setPreformato(numeracionBean.getPreFormato());
                numeracionBD.setPostFormato(numeracionBean.getPostFormato());
                numeracionBD.setLongitud(numeracionBean.getLongitud());
                numeracionRepository.save(numeracionBD);
                responseModel.setMessage("Numeración actualizada");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(numeracionBD.getId());
            }
            else{
                Numeracion numeracion = new Numeracion();
                numeracion.setArea(area);
                numeracion.setTipoDocumento(tipoDocumento);
                numeracion.setTipo(numeracionBean.getTipoNumeracion());
                numeracion.setValor(numeracionBean.getNumeroActual());
                numeracion.setPreformato(numeracionBean.getPreFormato());
                numeracion.setPostFormato(numeracionBean.getPostFormato());
                numeracion.setLongitud(numeracionBean.getLongitud());
                numeracionRepository.save(numeracion);
                responseModel.setMessage("Numeración creada");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(numeracion.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}