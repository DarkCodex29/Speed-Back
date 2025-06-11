package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.Boton;
import com.hochschild.speed.back.model.domain.speed.Parametro;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.filter.ParametroFilter;

import java.util.List;

public interface ParametroService {
    List<Parametro> find(ParametroFilter filter);

    Parametro buscarPorId(Integer idParametro);

    List<Parametro> buscarParametroPorTipo(String tipo);

    Parametro obtenerPorTipoValor(String tipo, String valor);
}