package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.Cliente;

public interface SunatReniecService {
    Cliente consultaSunatReniec(Integer tipoDocumento, String nroDocumento);
}
