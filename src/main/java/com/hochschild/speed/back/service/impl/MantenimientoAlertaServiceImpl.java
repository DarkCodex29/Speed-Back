package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.AlertaBean;
import com.hochschild.speed.back.model.bean.mantenimiento.AlertaCleanBean;
import com.hochschild.speed.back.model.bean.mantenimiento.GridBean;
import com.hochschild.speed.back.model.bean.mantenimiento.TipoAlertaBean;
import com.hochschild.speed.back.model.domain.speed.Alerta;
import com.hochschild.speed.back.model.domain.speed.Grid;
import com.hochschild.speed.back.model.domain.speed.TipoAlerta;
import com.hochschild.speed.back.model.filter.mantenimiento.AlertaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.AlertaRepository;
import com.hochschild.speed.back.repository.speed.GridRepository;
import com.hochschild.speed.back.repository.speed.TipoAlertaRepository;
import com.hochschild.speed.back.service.MantenimientoAlertaService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MantenimientoAlertaServiceImpl implements MantenimientoAlertaService{
    private static final Logger LOGGER = Logger.getLogger(MantenimientoAlertaServiceImpl.class.getName());
    private final AlertaRepository alertaRepository;
    private final GridRepository gridRepository;
    private final TipoAlertaRepository tipoAlertaRepository;
    public MantenimientoAlertaServiceImpl(AlertaRepository alertaRepository,
                                          TipoAlertaRepository tipoAlertaRepository,
                                          GridRepository gridRepository) {
        this.alertaRepository = alertaRepository;
        this.tipoAlertaRepository = tipoAlertaRepository;
        this.gridRepository = gridRepository;
    }

    @Override
    public AlertaCleanBean find(Integer id) {

        AlertaCleanBean alertaCleanBean = new AlertaCleanBean();
        Alerta alerta = alertaRepository.findById(id);
        alertaCleanBean.setId(alerta.getId());

        GridBean grid = new GridBean();
        grid.setId(alerta.getGrid() != null ? alerta.getGrid().getId() : null);
        grid.setTitulo(alerta.getGrid() != null ? alerta.getGrid().getTitulo() : null);
        grid.setCodigo(alerta.getGrid() != null ? alerta.getGrid().getCodigo() : null);
        alertaCleanBean.setGrid(grid);

        TipoAlertaBean tipoAlertaBean = new TipoAlertaBean();
        tipoAlertaBean.setId(alerta.getTipoAlerta() != null ? alerta.getTipoAlerta().getId() : null);
        tipoAlertaBean.setNombre(alerta.getTipoAlerta() != null ? alerta.getTipoAlerta().getNombre() : null);
        tipoAlertaBean.setImagen(alerta.getTipoAlerta() != null ? alerta.getTipoAlerta().getImagen() : null);
        tipoAlertaBean.setPorcentajeIntervalo(alerta.getTipoAlerta() != null ? alerta.getTipoAlerta().getPorcentaje() : null);
        tipoAlertaBean.setDefecto(alerta.getTipoAlerta() != null && alerta.getTipoAlerta().isDefecto());
        alertaCleanBean.setTipoAlerta(tipoAlertaBean);

        return alertaCleanBean;
    }

    @Override
    public List<AlertaCleanBean> list(AlertaFilter filter) {

        List<AlertaCleanBean> alertas = new ArrayList<>();
        List<Alerta> alertasBD = alertaRepository.list(filter.getTitulo(),filter.getNombre());

        for (Alerta alerta : alertasBD) {
            AlertaCleanBean alertaCleanBean = new AlertaCleanBean();
            alertaCleanBean.setId(alerta.getId());

            GridBean grid = new GridBean();
            grid.setId(alerta.getGrid() != null ? alerta.getGrid().getId() : null);
            grid.setTitulo(alerta.getGrid() != null ? alerta.getGrid().getTitulo() : null);
            grid.setCodigo(alerta.getGrid() != null ? alerta.getGrid().getCodigo() : null);
            alertaCleanBean.setGrid(grid);

            TipoAlertaBean tipoAlertaBean = new TipoAlertaBean();
            tipoAlertaBean.setId(alerta.getTipoAlerta() != null ? alerta.getTipoAlerta().getId() : null);
            tipoAlertaBean.setNombre(alerta.getTipoAlerta() != null ? alerta.getTipoAlerta().getNombre() : null);
            tipoAlertaBean.setImagen(alerta.getTipoAlerta() != null ? alerta.getTipoAlerta().getImagen() : null);
            tipoAlertaBean.setPorcentajeIntervalo(alerta.getTipoAlerta() != null ? alerta.getTipoAlerta().getPorcentaje() : null);
            tipoAlertaBean.setDefecto(alerta.getTipoAlerta() != null && alerta.getTipoAlerta().isDefecto());
            alertaCleanBean.setTipoAlerta(tipoAlertaBean);

            alertas.add(alertaCleanBean);
        }
        return alertas;
    }

    @Override
    public List<Grid> listGrids() {

        List<Grid> grids = gridRepository.list();

        for (Grid grid :grids) {
            grid.setColumnas(null);
            grid.setBotones(null);
        }

        return grids;
    }

    @Override
    public List<TipoAlerta> listTipoAlertas() {
        return tipoAlertaRepository.list(null, null);
    }

    @Override
    public ResponseModel save(AlertaBean alertaBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            Grid grid = gridRepository.findById(alertaBean.getIdGrid());
            TipoAlerta tipoAlerta = tipoAlertaRepository.findById(alertaBean.getIdTipoAlerta());
            if (alertaBean.getId() != null){
                Alerta alertaBD = alertaRepository.findById(alertaBean.getId());
                alertaBD.setGrid(grid);
                alertaBD.setTipoAlerta(tipoAlerta);
                alertaRepository.save(alertaBD);
                responseModel.setMessage("Alerta actualizada");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(alertaBD.getId());
            }else{
                Alerta alerta = new Alerta();
                alerta.setGrid(grid);
                alerta.setTipoAlerta(tipoAlerta);
                alertaRepository.save(alerta);
                responseModel.setMessage("Alerta creada");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(alerta.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}