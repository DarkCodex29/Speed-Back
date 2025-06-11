package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.SedeBean;
import com.hochschild.speed.back.model.domain.speed.Sede;
import com.hochschild.speed.back.model.domain.speed.Ubigeo;
import com.hochschild.speed.back.model.filter.mantenimiento.SedeFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.SedeRepository;
import com.hochschild.speed.back.repository.speed.UbigeoRepository;
import com.hochschild.speed.back.service.MantenimientoSedeService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("MantenimientoSedeService")
public class MantenimientoSedeServiceImpl implements MantenimientoSedeService{
    private static final Logger LOGGER = Logger.getLogger(MantenimientoSedeServiceImpl.class.getName());
    private final SedeRepository sedeRepository;
    private final UbigeoRepository ubigeoRepository;
    public MantenimientoSedeServiceImpl(SedeRepository sedeRepository,
                                        UbigeoRepository ubigeoRepository) {
        this.sedeRepository = sedeRepository;
        this.ubigeoRepository = ubigeoRepository;
    }

    @Override
    public Sede find(Integer id) {
        return sedeRepository.findById(id);
    }

    @Override
    public List<Sede> list(SedeFilter sedeFilter) {
        return sedeRepository.list(sedeFilter.getNombre());
    }

    @Override
    public List<Ubigeo> listDepartamentos() {
        return ubigeoRepository.obtenerDepartamentos();
    }

    @Override
    public List<Ubigeo> listProvincias(Integer id) {
        return ubigeoRepository.obtenerPorPadre(id);
    }

    @Override
    public List<Ubigeo> listDistritos(Integer id) {
        return ubigeoRepository.obtenerPorPadre(id);
    }

    @Override
    public ResponseModel save(SedeBean sedeBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            Ubigeo ubigeo = ubigeoRepository.findById(sedeBean.getIdUbigeo());
            Sede sedeBD = sedeRepository.findById(sedeBean.getId());
            if (sedeBean.getId() != null){
                sedeBD.setEstado(sedeBean.getEstado() ? 'A' : 'I');
                sedeBD.setFechaCreacion(new Date());
                sedeBD.setNombre(sedeBean.getNombre());
                sedeBD.setUbigeo(ubigeo);
                sedeRepository.save(sedeBD);
                responseModel.setMessage("Sede actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(sedeBD.getId());
            }else{
                Sede sede = new Sede();
                sede.setEstado(sedeBean.getEstado() ? 'A' : 'I');
                sede.setFechaCreacion(new Date());
                sede.setNombre(sedeBean.getNombre());
                sede.setUbigeo(ubigeo);
                sedeRepository.save(sede);
                responseModel.setMessage("Sede creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(sede.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}