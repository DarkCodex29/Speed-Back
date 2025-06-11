package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.TipoCliente;
import com.hochschild.speed.back.model.filter.TipoClienteFilter;

import java.util.List;

public interface TipoClienteService {
    List<TipoCliente> find(TipoClienteFilter filter);
}
