package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.AlfrescoConfig;
import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.model.bean.mantenimiento.PlantillaBean;
import com.hochschild.speed.back.model.domain.speed.HcPlantilla;
import com.hochschild.speed.back.model.domain.speed.HcTipoContrato;
import com.hochschild.speed.back.model.filter.mantenimiento.PlantillaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.HcPlantillaRepository;
import com.hochschild.speed.back.repository.speed.HcTipoContratoRepository;
import com.hochschild.speed.back.service.AlfrescoService;
import com.hochschild.speed.back.service.MantenimientoPlantillaService;
import com.hochschild.speed.back.util.StringUtils;
import com.hochschild.speed.back.util.exception.ExcepcionAlfresco;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MantenimientoPlantillaService")
public class MantenimientoPlantillaServiceImpl implements MantenimientoPlantillaService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoPlantillaServiceImpl.class.getName());
    private final HcPlantillaRepository hcPlantillaRepository;
    private final HcTipoContratoRepository hcTipoContratoRepository;
    private final AlfrescoService alfrescoService;
    private final AlfrescoConfig alfrescoConfig;
    private final CydocConfig cydocConfig;

    public MantenimientoPlantillaServiceImpl(HcPlantillaRepository hcPlantillaRepository,
                                             HcTipoContratoRepository hcTipoContratoRepository,
                                             AlfrescoService alfrescoService,
                                             AlfrescoConfig alfrescoConfig,
                                             CydocConfig cydocConfig) {
        this.hcPlantillaRepository = hcPlantillaRepository;
        this.hcTipoContratoRepository = hcTipoContratoRepository;
        this.alfrescoService = alfrescoService;
        this.alfrescoConfig = alfrescoConfig;
        this.cydocConfig = cydocConfig;
    }

    @Override
    public HcPlantilla find(Integer id) {
        return hcPlantillaRepository.findById(id);
    }

    @Override
    public List<HcPlantilla> list(PlantillaFilter plantillaFilter) {
        return hcPlantillaRepository.list(plantillaFilter.getNombre(), plantillaFilter.getTipoContrato());
    }

    @Override
    public List<HcTipoContrato> listContratos() {
        return hcPlantillaRepository.listTipoContrato();
    }

    @Override
    public ResponseModel save(PlantillaBean plantillaBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            HcTipoContrato hcTipoContrato = hcTipoContratoRepository.findById(plantillaBean.getIdTipoContrato());
            String rutaPlantilla = null;

            try {
                if (StringUtils.isNotEmpty(plantillaBean.getRuta()) && StringUtils.isNotEmpty(plantillaBean.getName())) {
                    rutaPlantilla = (alfrescoService.subirArchivoPlantilla(alfrescoConfig.getSpacePlantillas(),
                            cydocConfig.getCarpetaArchivosSubidos() + "/" + plantillaBean.getRuta(),
                            plantillaBean.getName()));
                }
            } catch (ExcepcionAlfresco ex) {
                LOGGER.info(ex.getMessage());
                throw new RuntimeException(ex);
            }

            if (plantillaBean.getId() != null) {
                HcPlantilla hcPlantillaBD = hcPlantillaRepository.findById(plantillaBean.getId());
                hcPlantillaBD.setEstado(plantillaBean.getEstado() ? 'A' : 'I');
                hcPlantillaBD.setNombre(plantillaBean.getNombre());
                if (StringUtils.isNotEmpty(rutaPlantilla)) {
                    hcPlantillaBD.setRuta(rutaPlantilla);
                }
                hcPlantillaBD.setTipoContrato(hcTipoContrato);
                hcPlantillaRepository.save(hcPlantillaBD);
                responseModel.setMessage("Plantilla actualizada");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(hcPlantillaBD.getId());
            } else {
                HcPlantilla hcPlantilla = new HcPlantilla();
                hcPlantilla.setEstado(plantillaBean.getEstado() ? 'A' : 'I');
                hcPlantilla.setNombre(plantillaBean.getNombre());
                hcPlantilla.setRuta(rutaPlantilla);
                hcPlantilla.setTipoContrato(hcTipoContrato);
                hcPlantillaRepository.save(hcPlantilla);
                responseModel.setMessage("Plantilla creada");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(hcPlantilla.getId());
            }
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}