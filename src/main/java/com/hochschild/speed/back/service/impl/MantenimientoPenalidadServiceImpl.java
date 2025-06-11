package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.PenalidadBean;
import com.hochschild.speed.back.model.bean.mantenimiento.ReiteranciasBean;
import com.hochschild.speed.back.model.bean.mantenimiento.TipoValorBean;
import com.hochschild.speed.back.model.domain.speed.HcPenalidad;
import com.hochschild.speed.back.model.filter.mantenimiento.PenalidadFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.HcPenalidadRepository;
import com.hochschild.speed.back.service.MantenimientoPenalidadService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("MantenimientoPenalidadService")
public class MantenimientoPenalidadServiceImpl implements MantenimientoPenalidadService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoPenalidadServiceImpl.class.getName());
    private final HcPenalidadRepository hcPenalidadRepository;

    public MantenimientoPenalidadServiceImpl(HcPenalidadRepository hcPenalidadRepository) {
        this.hcPenalidadRepository = hcPenalidadRepository;
    }

    @Override
    public HcPenalidad find(Integer id) {
        return hcPenalidadRepository.findById(id);
    }

    @Override
    public List<HcPenalidad> list(PenalidadFilter penalidadFilter) {
        return hcPenalidadRepository.list(penalidadFilter.getDescripcion(),
                                          penalidadFilter.getReiterancia());
    }

    @Override
    public List<ReiteranciasBean> listReiterancias() {

        List<ReiteranciasBean> reiterancias = new ArrayList<>();

        for (int i = 1; i < 10; i++) {
            ReiteranciasBean reiteranciasBean = new ReiteranciasBean();
            reiteranciasBean.setId(i);
            reiteranciasBean.setValor(String.valueOf(i));
            reiterancias.add(reiteranciasBean);
        }
        return reiterancias;
    }

    @Override
    public List<TipoValorBean> listTipoValores() {

        List<TipoValorBean> tipoValores = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
            TipoValorBean reiteranciasBean = new TipoValorBean();
            reiteranciasBean.setId(i);
            reiteranciasBean.setValor("Sin Tipo Moneda");
            tipoValores.add(reiteranciasBean);
        }
        return tipoValores;
    }

    @Override
    public ResponseModel save(PenalidadBean penalidadBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            if (penalidadBean.getId() != null){

                HcPenalidad hcPenalidadBD = hcPenalidadRepository.findById(penalidadBean.getId());
                hcPenalidadBD.setDescripcion(penalidadBean.getPenalidad());
                hcPenalidadBD.setReiterancia(penalidadBean.getIdReiterancia() != null ? Integer.valueOf(penalidadBean.getIdReiterancia()) : null);
                hcPenalidadBD.setAplicaPenalidad(penalidadBean.getAplicaPenalidad());
                hcPenalidadBD.setEstado(penalidadBean.getEstado());
                hcPenalidadBD.setEtiqueta(penalidadBean.getEtiqueta());
                hcPenalidadBD.setAplicaPorDefecto(penalidadBean.getAplicaValorDefecto());
                hcPenalidadBD.setNumeroReiterancia(penalidadBean.getNumeroReiterancia() != null ? Integer.valueOf(penalidadBean.getNumeroReiterancia()) : null);
                hcPenalidadBD.setTipoValor(penalidadBean.getIdTipoValor());
                hcPenalidadBD.setValor(penalidadBean.getValor());
                hcPenalidadRepository.save(hcPenalidadBD);
                responseModel.setMessage("Penalidad actualizada");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(hcPenalidadBD.getId());
            }else{
                HcPenalidad hcPenalidad = new HcPenalidad();
                hcPenalidad.setDescripcion(penalidadBean.getPenalidad());
                hcPenalidad.setReiterancia(penalidadBean.getIdReiterancia() != null ? Integer.valueOf(penalidadBean.getIdReiterancia()) : null);
                hcPenalidad.setAplicaPenalidad(penalidadBean.getAplicaPenalidad());
                hcPenalidad.setEstado(penalidadBean.getEstado());
                hcPenalidad.setEtiqueta(penalidadBean.getEtiqueta());
                hcPenalidad.setAplicaPorDefecto(penalidadBean.getAplicaValorDefecto());
                hcPenalidad.setNumeroReiterancia(penalidadBean.getNumeroReiterancia() != null ? Integer.valueOf(penalidadBean.getNumeroReiterancia()) : null);
                hcPenalidad.setTipoValor(penalidadBean.getIdTipoValor());
                hcPenalidad.setValor(penalidadBean.getValor());
                hcPenalidadRepository.save(hcPenalidad);
                responseModel.setMessage("Penalidad creada");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(hcPenalidad.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}