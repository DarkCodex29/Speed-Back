package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.RolBean;
import com.hochschild.speed.back.model.domain.speed.Rol;
import com.hochschild.speed.back.model.filter.mantenimiento.RolFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.RolRepository;
import com.hochschild.speed.back.service.MantenimientoRolesService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class MantenimientoRolesServiceImpl implements MantenimientoRolesService{
    private static final Logger LOGGER = Logger.getLogger(MantenimientoRolesServiceImpl.class.getName());
    private final RolRepository rolRepository;

    public MantenimientoRolesServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public Rol find(Integer id) {
        return rolRepository.findById(id);
    }

    @Override
    public List<Rol> list(RolFilter filter) {
        return rolRepository.list(filter.getNombre());
    }

    @Override
    public ResponseModel save(RolBean rolBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            if (rolBean.getId() != null){
                Rol rolBD = rolRepository.findById(rolBean.getId());
                rolBD.setCodigo(rolBean.getCodigo());
                rolBD.setDescripcion(rolBean.getDescripcion());
                rolBD.setEstado(rolBean.getEstado() ? 'A' : 'I');
                rolBD.setFechaCreacion(new Date());
                rolBD.setNombre(rolBean.getNombre());
                rolBD.setCodigoSCA(rolBean.getCodigoSCA());
                rolRepository.save(rolBD);
                responseModel.setMessage("Rol actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(rolBD.getId());
            }else{
                Rol rol = new Rol();
                rol.setCodigo(rolBean.getCodigo());
                rol.setDescripcion(rolBean.getDescripcion());
                rol.setEstado(rolBean.getEstado() ? 'A' : 'I');
                rol.setFechaCreacion(new Date());
                rol.setNombre(rolBean.getNombre());
                rol.setCodigoSCA(rolBean.getCodigoSCA());
                rolRepository.save(rol);
                responseModel.setMessage("Rol creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(rol.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}