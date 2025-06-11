package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.DataProcesoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.domain.speed.Parametro;
import com.hochschild.speed.back.model.domain.speed.TipoProceso;
import com.hochschild.speed.back.model.filter.mantenimiento.ProcesoFilter;
import com.hochschild.speed.back.model.bean.mantenimiento.ProcesoBean;
import com.hochschild.speed.back.model.domain.speed.TipoDocumento;
import org.springframework.transaction.annotation.Transactional;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface MantenimientoProcesoService {
    ResponseEntity<List<TipoDocumento>> getTiposDocumentos();
    @Transactional
    ResponseEntity<List<Usuario>> obtenerTodosUsuariosActivos();
    DataProcesoBean find(Integer id);
    List<DataProcesoBean> list(ProcesoFilter procesoFilter);
    List<TipoProceso> listTipoProcesos();
    List<Parametro> listConfidencialidad();
    List<UsuariosBean> listUsuarios();
    List<UsuariosBean> listUsuariosDisponibles(Integer idUsuario);
    List<TipoDocumento> listTipoDocumento();
    List<TipoDocumento> listTipoDocumentoDisponibles(Integer idUsuario);
    ResponseModel save(ProcesoBean procesoBean);
}
