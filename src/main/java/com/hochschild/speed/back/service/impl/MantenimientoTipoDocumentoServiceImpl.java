package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.CampoDocumentoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.TipoDocumentoBean;
import com.hochschild.speed.back.model.domain.speed.Campo;
import com.hochschild.speed.back.model.domain.speed.TipoDocumento;
import com.hochschild.speed.back.model.filter.mantenimiento.TipoDocumentoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.CampoRepository;
import com.hochschild.speed.back.repository.speed.TipoDocumentoRepository;
import com.hochschild.speed.back.service.MantenimientoTipoDocumentoService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MantenimientoTipoDocumentoServiceImpl implements MantenimientoTipoDocumentoService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoTipoDocumentoServiceImpl.class.getName());
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final CampoRepository campoRepository;

    public MantenimientoTipoDocumentoServiceImpl(TipoDocumentoRepository tipoDocumentoRepository,
                                                 CampoRepository campoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.campoRepository = campoRepository;
    }

    @Override
    public TipoDocumento find(Integer id) {
        return tipoDocumentoRepository.findById(id);
    }

    @Override
    public List<TipoDocumento> list(TipoDocumentoFilter tipoDocumentoFilter) {
        return tipoDocumentoRepository.list(tipoDocumentoFilter.getDecripcion(), tipoDocumentoFilter.getNombre());
    }

    @Override
    public List<CampoDocumentoBean> listCampos() {

        List<CampoDocumentoBean> campos = new ArrayList<>();
        List<Campo> camposBD = campoRepository.list(null,null);

        for (Campo campo : camposBD) {
            CampoDocumentoBean campoDocumentoBean = new CampoDocumentoBean();
            campoDocumentoBean.setId(campo.getId());
            campoDocumentoBean.setNombre(campo.getNombre());
            campoDocumentoBean.setDescripcion(campo.getDescripcion());
            campos.add(campoDocumentoBean);
        }
        return campos;
    }

    @Override
    public List<CampoDocumentoBean> listCamposDisponibles(Integer id) {

        List<CampoDocumentoBean> campos = new ArrayList<>();
        List<Campo> camposBD = campoRepository.buscarCamposNoAsignados(id);

        for (Campo campo : camposBD) {
            CampoDocumentoBean campoDocumentoBean = new CampoDocumentoBean();
            campoDocumentoBean.setId(campo.getId());
            campoDocumentoBean.setNombre(campo.getNombre());
            campoDocumentoBean.setDescripcion(campo.getDescripcion());
            campos.add(campoDocumentoBean);
        }
        return campos;
    }

    @Override
    public ResponseModel save(TipoDocumentoBean tipoDocumentoBean) {

        ResponseModel responseModel = new ResponseModel();
        List<Campo> campos = new ArrayList<>();

        try {

            for (CampoDocumentoBean campoDocumentoBean : tipoDocumentoBean.getCampos()) {
                Campo campoBD = campoRepository.findById(campoDocumentoBean.getId());
                campos.add(campoBD);
            }

            if (tipoDocumentoBean.getId() != null){
                TipoDocumento tipoDocumentoBD = tipoDocumentoRepository.findById(tipoDocumentoBean.getId());
                tipoDocumentoBD.setDescripcion(tipoDocumentoBean.getDescripcion());
                tipoDocumentoBD.setEstado(tipoDocumentoBean.getEstado() ? 'A' : 'I');
                tipoDocumentoBD.setFechaCreacion(new Date());
                tipoDocumentoBD.setFirmable(tipoDocumentoBean.getFirmable());
                tipoDocumentoBD.setNombre(tipoDocumentoBean.getNombre());
                tipoDocumentoBD.setCampos(campos);
                tipoDocumentoRepository.save(tipoDocumentoBD);
                responseModel.setMessage("Tipo documento actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(tipoDocumentoBD.getId());
            }else{
                TipoDocumento tipoDocumento = new TipoDocumento();
                tipoDocumento.setDescripcion(tipoDocumentoBean.getDescripcion());
                tipoDocumento.setEstado(tipoDocumentoBean.getEstado() ? 'A' : 'I');
                tipoDocumento.setFechaCreacion(new Date());
                tipoDocumento.setFirmable(tipoDocumentoBean.getFirmable());
                tipoDocumento.setNombre(tipoDocumentoBean.getNombre());
                tipoDocumento.setCampos(campos);
                tipoDocumentoRepository.save(tipoDocumento);
                responseModel.setMessage("Tipo documento creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(tipoDocumento.getId());
            }
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}