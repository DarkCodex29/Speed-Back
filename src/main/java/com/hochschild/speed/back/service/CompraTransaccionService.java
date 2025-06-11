package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.CompraTransaccionBean;
import com.hochschild.speed.back.model.response.ResponseModel;

import java.util.List;

public interface CompraTransaccionService {

    List<CompraTransaccionBean> listarByEstado(String descripcion);

    ResponseModel guardar(CompraTransaccionBean model, Integer idUsuario);

    ResponseModel eliminar(Integer idModel, Integer idUsuario);
}
