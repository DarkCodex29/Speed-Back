package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.DataProcesoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.ProcesoBean;
import com.hochschild.speed.back.model.domain.speed.Proceso;
import com.hochschild.speed.back.model.domain.speed.TipoDocumento;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.filter.mantenimiento.ProcesoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MantenimientoTipoProcesoService {

    ResponseEntity<List<TipoDocumento>> getTiposDocumentos();
    @Transactional
    ResponseEntity<List<Usuario>> obtenerTodosUsuariosActivos();
    Proceso find(Integer id);
    List<Proceso> list(ProcesoFilter procesoFilter);
    ResponseModel save(ProcesoBean procesoBean);
}
