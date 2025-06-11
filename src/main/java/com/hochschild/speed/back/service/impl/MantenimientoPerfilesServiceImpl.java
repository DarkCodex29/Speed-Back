package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.PerfilBean;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.filter.mantenimiento.PerfilFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.PerfilRepository;
import com.hochschild.speed.back.service.MantenimientoPerfilesService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MantenimientoPerfilesServiceImpl implements MantenimientoPerfilesService{
    private static final Logger LOGGER = Logger.getLogger(MantenimientoPerfilesServiceImpl.class.getName());
    private final PerfilRepository perfilRepository;
    public MantenimientoPerfilesServiceImpl(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @Override
    public Perfil find(Integer id) {
        return perfilRepository.findById(id);
    }

    @Override
    public List<Perfil> list(PerfilFilter filter) {
        return perfilRepository.list(filter.getNombre());
    }

    @Override
    public ResponseModel save(PerfilBean perfilBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            if (perfilBean.getId() != null){
                Perfil perfilBD = perfilRepository.findById(perfilBean.getId());
                perfilBD.setCodigo(perfilBean.getCodigo());
                perfilBD.setDescripcion(perfilBean.getDescripcion());
                perfilBD.setEstado(perfilBean.getEstado() ? 'A' : 'I');
                perfilBD.setFechaCreacion(new Date());
                perfilBD.setNombre(perfilBean.getNombre());
                perfilRepository.save(perfilBD);
                responseModel.setMessage("Perfil actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(perfilBD.getId());
            }else{
                Perfil perfil = new Perfil();
                perfil.setCodigo(perfilBean.getCodigo());
                perfil.setDescripcion(perfilBean.getDescripcion());
                perfil.setEstado(perfilBean.getEstado() ? 'A' : 'I');
                perfil.setFechaCreacion(new Date());
                perfil.setNombre(perfilBean.getNombre());
                perfilRepository.save(perfil);
                responseModel.setMessage("Perfil creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(perfil.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}