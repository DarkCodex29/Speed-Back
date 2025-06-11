package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.FeriadoBean;
import com.hochschild.speed.back.model.domain.speed.Feriado;
import com.hochschild.speed.back.model.domain.speed.Sede;
import com.hochschild.speed.back.model.filter.mantenimiento.FeriadoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.FeriadoRepository;
import com.hochschild.speed.back.repository.speed.SedeRepository;
import com.hochschild.speed.back.service.MantenimientoFeriadoService;
import com.hochschild.speed.back.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("MantenimientoFeriadoService")
public class MantenimientoFeriadoServiceImpl implements MantenimientoFeriadoService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoFeriadoServiceImpl.class.getName());
    private final FeriadoRepository feriadoRepository;
    private final SedeRepository sedeRepository;
    public MantenimientoFeriadoServiceImpl(FeriadoRepository feriadoRepository,
                                           SedeRepository sedeRepository) {
        this.feriadoRepository = feriadoRepository;
        this.sedeRepository = sedeRepository;
    }

    @Override
    public Feriado find(Integer id) {
        return feriadoRepository.findById(id);
    }

    @Override
    public List<Feriado> list(FeriadoFilter feriadoFilter) {
        return feriadoRepository.list(feriadoFilter.getNombre());
    }

    @Override
    public ResponseModel save(FeriadoBean feriadoBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            Sede sede = sedeRepository.findById(feriadoBean.getIdSede());

            if (feriadoBean.getId() != null){
                Feriado feriadoBD = feriadoRepository.findById(feriadoBean.getId());
                feriadoBD.setFecha(DateUtil.convertStringToDate(feriadoBean.getFecha(), DateUtil.FORMAT_DATE_XML));
                feriadoBD.setFechaCreacion(new Date());
                feriadoBD.setEstado(feriadoBean.getEstado() ? 'A' : 'I');
                feriadoBD.setSede(sede);
                feriadoRepository.save(feriadoBD);
                responseModel.setMessage("Feriado actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(feriadoBD.getId());
            }else{
                Feriado feriado = new Feriado();
                feriado.setFecha(DateUtil.convertStringToDate(feriadoBean.getFecha(), DateUtil.FORMAT_DATE_XML));
                feriado.setFechaCreacion(new Date());
                feriado.setEstado(feriadoBean.getEstado() ? 'A' : 'I');
                feriado.setSede(sede);
                feriadoRepository.save(feriado);
                responseModel.setMessage("Feriado creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(feriado.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}