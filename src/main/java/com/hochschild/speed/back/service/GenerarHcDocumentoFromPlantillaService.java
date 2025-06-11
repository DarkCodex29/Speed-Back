package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.HcTipoContrato;
import com.hochschild.speed.back.model.domain.speed.HcDocumentoLegal;
import java.util.List;

public interface GenerarHcDocumentoFromPlantillaService {
    List<HcTipoContrato> findLstHcTipoContratoCodigoAdenda();

    List<HcTipoContrato> findLstHcTipoContratoCodigoAdendaContractual();

    String generarHcDocumentoFromPlantilla(HcDocumentoLegal documentoLegal, Integer idHcTipoContrato);

    Boolean validarPlantilla(HcDocumentoLegal documentoLegal, Integer idHcTipoContrato);
}