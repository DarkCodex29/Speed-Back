package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.ObservarVisadoBean;
import com.hochschild.speed.back.model.domain.speed.HcUsuarioPorVisado;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.domain.speed.utils.ElementoListaVisadoDTO;
import com.hochschild.speed.back.model.response.CancelarVisadoResponse;
import com.hochschild.speed.back.model.response.ResponseModel;
import org.springframework.http.ResponseEntity;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface VisadoService {
    List<HcUsuarioPorVisado> obtenerVisadores(Integer idExpediente);

    ResponseEntity<List<ElementoListaVisadoDTO>> obtenerListadoVisado(String usuario);

    ResponseModel aprobarVisado(Integer idExpediente, Integer idUsuario);

    Map<String, Object> obtenerDestinatario(Integer idExpediente);

    @Transactional
    ResponseModel observarVisado(ObservarVisadoBean observacion, Integer idUsuario);

    ResponseEntity<CancelarVisadoResponse> getInfoCancelarVisado(Integer idExpediente, Integer idUsuario);


    @Transactional
    ResponseModel cancelarVisado(Integer idExpediente, Integer idUsuario);
}
