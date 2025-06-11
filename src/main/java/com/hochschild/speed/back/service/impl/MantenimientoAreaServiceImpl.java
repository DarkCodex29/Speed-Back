package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.AreaBean;
import com.hochschild.speed.back.model.bean.mantenimiento.AreaPadreBean;
import com.hochschild.speed.back.model.bean.mantenimiento.PadreBean;
import com.hochschild.speed.back.model.domain.speed.Area;
import com.hochschild.speed.back.model.domain.speed.Sede;
import com.hochschild.speed.back.model.filter.mantenimiento.AreaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.AreaRepository;
import com.hochschild.speed.back.repository.speed.SedeRepository;
import com.hochschild.speed.back.service.MantenimientoAreaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MantenimientoAreaServiceImpl implements MantenimientoAreaService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoAreaServiceImpl.class.getName());
    final AreaRepository areaRepository;
    final SedeRepository sedeRepository;

    @Autowired
    public MantenimientoAreaServiceImpl(AreaRepository areaRepository,
                                        SedeRepository sedeRepository) {
        this.areaRepository = areaRepository;
        this.sedeRepository = sedeRepository;
    }

    @Override
    public List<Sede> buscarSedes() {
        return sedeRepository.obtenerActivas();
    }

    @Override
    public List<Area> buscarAreas() {
        return areaRepository.buscarAreas();
    }

    @Override
    public AreaPadreBean find(Integer id) {
        Area area = areaRepository.findById(id);
        Area dependencia = areaRepository.findById(area.getDependencia());

        AreaPadreBean areaFormat = new AreaPadreBean();
        PadreBean padreBean = new PadreBean();
        padreBean.setId(dependencia != null ? dependencia.getId() : null);
        padreBean.setNombre(dependencia != null ? dependencia.getNombre() : null);
        areaFormat.setId(area.getId());
        areaFormat.setNombre(area.getNombre());
        areaFormat.setSede(area.getSede());
        areaFormat.setDependencia(padreBean);

        return areaFormat;
    }

    @Override
    public List<AreaPadreBean> list(AreaFilter filter) {

        List<AreaPadreBean> areasPadre = new ArrayList<>();
        List<Area> areas = areaRepository.list(filter.getNombre());

        for (Area area : areas) {
            Area dependencia = areaRepository.findById(area.getDependencia());
            AreaPadreBean areaFormat = new AreaPadreBean();
            PadreBean padreBean = new PadreBean();
            padreBean.setId(dependencia != null ? dependencia.getId() : null);
            padreBean.setNombre(dependencia != null ? dependencia.getNombre() : null);

            areaFormat.setId(area.getId());
            areaFormat.setNombre(area.getNombre());
            areaFormat.setSede(area.getSede());
            areaFormat.setDependencia(padreBean);
            areasPadre.add(areaFormat);
        }

        return areasPadre;
    }

    @Override
    public ResponseModel save(AreaBean areaBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            Sede sede = sedeRepository.findById(areaBean.getIdSede());
            Area dependencia = areaRepository.findById(areaBean.getIdDependencia());

            if (areaBean.getId() != null){
                Area areaBD = areaRepository.findById(areaBean.getId());
                areaBD.setNombre(areaBean.getNombre());
                areaBD.setSede(sede);
                areaBD.setDependencia(dependencia.getId());
                areaBD.setFechaCreacion(new Date());
                areaRepository.save(areaBD);
                responseModel.setMessage("Area actualizada");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(areaBD.getId());
            }else{
                Area area = new Area();
                area.setNombre(areaBean.getNombre());
                area.setSede(sede);
                area.setDependencia(dependencia.getId());
                area.setFechaCreacion(new Date());
                areaRepository.save(area);
                responseModel.setMessage("Area creada");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(area.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}
