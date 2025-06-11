package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.PlantillaBean;
import com.hochschild.speed.back.model.domain.speed.HcPlantilla;
import com.hochschild.speed.back.model.domain.speed.HcTipoContrato;
import com.hochschild.speed.back.model.filter.mantenimiento.PlantillaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoPlantillaService {
    HcPlantilla find(Integer id);
    List<HcPlantilla> list(PlantillaFilter plantillaFilter);
    List<HcTipoContrato> listContratos();
    ResponseModel save(PlantillaBean plantillaBean);
}
