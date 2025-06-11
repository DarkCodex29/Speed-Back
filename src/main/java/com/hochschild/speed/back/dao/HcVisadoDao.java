package com.hochschild.speed.back.dao;

import com.hochschild.speed.back.model.domain.speed.HcVisado;
import com.hochschild.speed.back.model.domain.speed.utils.ElementoListaVisadoDTO;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface HcVisadoDao {
    HcVisado obtenerUltimoVisado(Integer idDocumentoLegal);

    List<ElementoListaVisadoDTO> obtenerListaVisado(String usuario);
}
