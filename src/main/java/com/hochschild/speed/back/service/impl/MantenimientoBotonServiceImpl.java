package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.BotonBean;
import com.hochschild.speed.back.model.bean.mantenimiento.BotonPerfilBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.filter.mantenimiento.BotonFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.BotonPorGridPorPerfilRepository;
import com.hochschild.speed.back.repository.speed.BotonRepository;
import com.hochschild.speed.back.repository.speed.GridRepository;
import com.hochschild.speed.back.repository.speed.PerfilRepository;
import com.hochschild.speed.back.service.MantenimientoBotonService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("MantenimientoBotonService")
public class MantenimientoBotonServiceImpl implements MantenimientoBotonService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoBotonServiceImpl.class.getName());
    private final BotonRepository botonRepository;
    private final PerfilRepository perfilRepository;
    private final BotonPorGridPorPerfilRepository botonPorGridPorPerfilRepository;
    private final GridRepository gridRepository;
    public MantenimientoBotonServiceImpl(BotonRepository botonRepository,
                                         PerfilRepository perfilRepository,
                                         BotonPorGridPorPerfilRepository botonPorGridPorPerfilRepository,
                                         GridRepository gridRepository) {
        this.botonRepository = botonRepository;
        this.perfilRepository = perfilRepository;
        this.botonPorGridPorPerfilRepository = botonPorGridPorPerfilRepository;
        this.gridRepository = gridRepository;
    }

    @Override
    public BotonPerfilBean find(Integer id) {

        BotonPerfilBean botonPerfilBean = new BotonPerfilBean();
        Boton boton = botonRepository.findById(id);

        List<BotonPorGridPorPerfil> perfilesEnGrid = botonPorGridPorPerfilRepository.findBotonesById(id);
        List<Perfil> perfiles = new ArrayList<>();

        for (BotonPorGridPorPerfil botonPorGridPorPerfil : perfilesEnGrid) {
            Perfil perfil = botonPorGridPorPerfil.getId().getPerfil();
            perfiles.add(perfil);
        }

        botonPerfilBean.setId(boton.getId());
        botonPerfilBean.setNombre(boton.getNombre());
        botonPerfilBean.setDescripcion(boton.getDescripcion());
        botonPerfilBean.setUrl(boton.getUrl());
        botonPerfilBean.setCodigo(boton.getCodigo());
        botonPerfilBean.setTipo(boton.getTipo());
        botonPerfilBean.setIcono(boton.getIcono());
        botonPerfilBean.setParametro(boton.getParametro());
        botonPerfilBean.setBloqueable(boton.getBloqueable());
        botonPerfilBean.setBloqueableParalelo(boton.getVerParalela());
        botonPerfilBean.setEstado(boton.getEstado() == 'A');
        botonPerfilBean.setEventoSubmit(boton.getOnSubmit());
        botonPerfilBean.setEventoComplete(boton.getOnComplete());
        botonPerfilBean.setOrden(boton.getOrden());
        botonPerfilBean.setIconoNuevo(boton.getIconoNuevo());
        botonPerfilBean.setBotonClaseNuevo(boton.getBotonClaseNuevo());
        botonPerfilBean.setUrlNuevo(boton.getUrlNuevo());
        
        botonPerfilBean.setPerfiles(perfiles);

        return botonPerfilBean;
    }

    @Override
    public List<Boton> list(BotonFilter botonFilter) {
        return botonRepository.list(botonFilter.getNombre(), botonFilter.getCodigo(), botonFilter.getDescripcion());
    }

    @Override
    public List<Perfil> listPerfilesDisponibles(Integer idBoton) {
        return botonRepository.buscarPerfilesNoAsignados(idBoton);
    }

    @Override
    @Transactional
    public ResponseModel save(BotonBean botonBean) {

        ResponseModel responseModel = new ResponseModel();

        try {

            if (botonBean.getId() != null){
                Boton botonBD = botonRepository.findById(botonBean.getId());

                botonBD.setId(382);
                botonBD.setNombre(botonBean.getNombre());
                botonBD.setDescripcion(botonBean.getDescripcion());
                botonBD.setFechaCreacion(new Date());
                botonBD.setMovil(false);

                botonBD.setUrl(botonBean.getUrl());
                botonBD.setCodigo(botonBean.getCodigo());
                botonBD.setTipo(botonBean.getTipo());
                botonBD.setIcono(botonBean.getIcono());
                botonBD.setParametro(botonBean.getParametro());
                botonBD.setBloqueable(botonBean.getBloqueable());
                botonBD.setVerParalela(botonBean.getBloqueableParalelo());
                botonBD.setEstado(botonBean.getEstado() ? 'A' : 'I');
                botonBD.setOnSubmit(botonBean.getEventoSubmit());
                botonBD.setOnComplete(botonBean.getEventoComplete());
                botonBD.setOrden(botonBean.getOrden());
                botonBD.setIconoNuevo(null);
                botonBD.setBotonClaseNuevo(null);
                botonBD.setUrlNuevo(botonBean.getUrlNuevo());

                botonRepository.save(botonBD);
                responseModel.setMessage("Boton actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(botonBD.getId());
            }else{
                Boton boton = new Boton();

                boton.setId(382);
                boton.setNombre(botonBean.getNombre());
                boton.setDescripcion(botonBean.getDescripcion());
                boton.setFechaCreacion(new Date());
                boton.setMovil(false);

                boton.setUrl(botonBean.getUrl());
                boton.setCodigo(botonBean.getCodigo());
                boton.setTipo(botonBean.getTipo());
                boton.setIcono(botonBean.getIcono());
                boton.setParametro(botonBean.getParametro());
                boton.setBloqueable(botonBean.getBloqueable());
                boton.setVerParalela(botonBean.getBloqueableParalelo());
                boton.setOnSubmit(botonBean.getEventoSubmit());
                boton.setOnComplete(botonBean.getEventoComplete());
                boton.setOrden(botonBean.getOrden());
                boton.setIconoNuevo(null);
                boton.setBotonClaseNuevo(null);
                boton.setUrlNuevo(botonBean.getUrlNuevo());
                botonRepository.save(boton);
                responseModel.setMessage("Boton creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(boton.getId());
            }

            botonPorGridPorPerfilRepository.eliminarBotonesPorGridPorPerfil(responseModel.getId());

            for (Perfil perfil : botonBean.getPerfiles()) {

                Perfil perfilBD = perfilRepository.findById(perfil.getId());
                Boton botonBD = botonRepository.findById(responseModel.getId());
                Grid gridBD = gridRepository.findById(206);

                BotonPorGridPorPerfil perfilenGrid = new BotonPorGridPorPerfil();
                BotonPorGridPorPerfilPK botonPorGridPorPerfilPK = new BotonPorGridPorPerfilPK();
                botonPorGridPorPerfilPK.setBoton(botonBD);
                botonPorGridPorPerfilPK.setPerfil(perfilBD);
                botonPorGridPorPerfilPK.setGrid(gridBD);
                perfilenGrid.setId(botonPorGridPorPerfilPK);

                botonPorGridPorPerfilRepository.save(perfilenGrid);
            }

        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}