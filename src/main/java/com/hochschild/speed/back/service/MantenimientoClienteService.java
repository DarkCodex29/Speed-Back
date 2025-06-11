package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.VerificacionClienteBean;
import com.hochschild.speed.back.model.bean.mantenimiento.ClienteBean;
import com.hochschild.speed.back.model.domain.speed.Cliente;
import com.hochschild.speed.back.model.domain.speed.TipoCliente;
import com.hochschild.speed.back.model.filter.BuscarClienteFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface MantenimientoClienteService {

    ResponseEntity<Cliente> buscarCliente(Integer idCliente);

    ResponseEntity<List<Cliente>> listarCliente();

    ResponseEntity<List<Cliente>> listarCliente(BuscarClienteFilter filter);

    ResponseEntity<List<TipoCliente>> obtenerTiposActivos();

    ResponseEntity<VerificacionClienteBean> verificarExistenciaCliente(String dni);

    ResponseEntity<VerificacionClienteBean> verificarExistenciaClienteContraparte(String dni);

    ResponseModel guardarCliente(ClienteBean clienteBean, Integer idUsuario);
}
