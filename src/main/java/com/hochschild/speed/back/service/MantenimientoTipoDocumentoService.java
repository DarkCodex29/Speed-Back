package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.CampoDocumentoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.TipoDocumentoBean;
import com.hochschild.speed.back.model.domain.speed.TipoDocumento;
import com.hochschild.speed.back.model.filter.mantenimiento.TipoDocumentoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoTipoDocumentoService {
    TipoDocumento find(Integer id);
    List<TipoDocumento> list(TipoDocumentoFilter tipoDocumentoFilter);
    List<CampoDocumentoBean> listCampos();
    List<CampoDocumentoBean> listCamposDisponibles(Integer id);
    ResponseModel save(TipoDocumentoBean tipoDocumentoBean);
}
