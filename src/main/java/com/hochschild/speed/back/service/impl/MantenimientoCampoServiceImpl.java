package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.CampoBean;
import com.hochschild.speed.back.model.domain.speed.Campo;
import com.hochschild.speed.back.model.domain.speed.TipoCampo;
import com.hochschild.speed.back.model.filter.mantenimiento.CampoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.CampoRepository;
import com.hochschild.speed.back.repository.speed.ParametroRepository;
import com.hochschild.speed.back.repository.speed.TipoCampoRepository;
import com.hochschild.speed.back.service.MantenimientoCampoService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MantenimientoCampoServiceImpl implements MantenimientoCampoService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoCampoServiceImpl.class.getName());
    private final ParametroRepository parametroRepository;
    private final CampoRepository campoRepository;
    private final TipoCampoRepository tipoCampoRepository;
    public MantenimientoCampoServiceImpl(ParametroRepository parametroRepository,
                                         CampoRepository campoRepository,
                                         TipoCampoRepository tipoCampoRepository) {
        this.parametroRepository = parametroRepository;
        this.campoRepository = campoRepository;
        this.tipoCampoRepository = tipoCampoRepository;
    }

    @Override
    public Campo find(Integer id) {
        return campoRepository.findById(id);
    }

    @Override
    public List<Campo> list(CampoFilter filter) {
        return campoRepository.list(filter.getDescripcion(), filter.getNombre());
    }

    @Override
    public List<TipoCampo> fieldType() {
        return tipoCampoRepository.obtenerActivos();
    }

    @Override
    public List<String> parameterType() {
        return parametroRepository.parameterType();
    }

    @Override
    public ResponseModel save(CampoBean campoBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            TipoCampo tipoCampo = tipoCampoRepository.findById(campoBean.getIdTipoCampo());
            if (campoBean.getId() != null){
                Campo campoBD = campoRepository.findById(campoBean.getId());
                campoBD.setBuscable(campoBean.getBuscable());
                campoBD.setContenido(campoBean.getContenido());
                campoBD.setDescripcion(campoBean.getDescripcion());
                campoBD.setEtiqueta(campoBean.getEtiqueta());
                campoBD.setFechaCreacion(new Date());
                campoBD.setNombre(campoBean.getNombre());
                campoBD.setObligatorio(campoBean.getObligatorio());
                campoBD.setTipoCampo(tipoCampo);
                campoRepository.save(campoBD);
                responseModel.setMessage("Campo actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(campoBD.getId());
            }
            else{
                Campo campo = new Campo();
                campo.setBuscable(campoBean.getBuscable());
                campo.setContenido(campoBean.getContenido());
                campo.setDescripcion(campoBean.getDescripcion());
                campo.setEtiqueta(campoBean.getEtiqueta());
                campo.setFechaCreacion(new Date());
                campo.setNombre(campoBean.getNombre());
                campo.setObligatorio(campoBean.getObligatorio());
                campo.setTipoCampo(tipoCampo);
                campoRepository.save(campo);
                responseModel.setMessage("Campo creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(campo.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}